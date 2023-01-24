package com.decagon.rewardyourteacherapi.serviceImpl;


import com.decagon.rewardyourteacherapi.OAuth.CustomOAuth2User;
import com.decagon.rewardyourteacherapi.configuration.PasswordConfig;
import com.decagon.rewardyourteacherapi.dto.*;
import com.decagon.rewardyourteacherapi.entity.*;
import com.decagon.rewardyourteacherapi.enums.Provider;
import com.decagon.rewardyourteacherapi.enums.Roles;
import com.decagon.rewardyourteacherapi.exception.EmailAlreadyExistsException;
import com.decagon.rewardyourteacherapi.exception.UserNotFoundException;
import com.decagon.rewardyourteacherapi.repository.*;
import com.decagon.rewardyourteacherapi.response.ResponseAPI;
import com.decagon.rewardyourteacherapi.security.jwt.JWTTokenProvider;
import com.decagon.rewardyourteacherapi.service.UserService;
import com.decagon.rewardyourteacherapi.utils.AuthDetails;
import com.decagon.rewardyourteacherapi.utils.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.decagon.rewardyourteacherapi.enums.TransactionType.DEBIT;

import static org.springframework.http.HttpStatus.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final WalletRepository walletRepository;

    private final SubjectsRepository subjectsRepository;

    private final NotificationRepository notificationRepository;

    private final TeacherRepository teacherRepository;
    private final TransactionRepository transactionRepository;

    private final StudentRepository studentRepository;


    private PasswordEncoder passwordEncoder;

    private final MailService mailService;


    private final JWTTokenProvider jwtTokenProvider;
    private final AuthDetails authDetails;


    @Autowired
    public UserServiceImpl(WalletRepository walletRepository, UserRepository userRepository, SubjectsRepository subjectsRepository,
                           TeacherRepository teacherRepository, StudentRepository studentRepository,
                           JWTTokenProvider jwtTokenProvider, NotificationRepository notificationRepository,
                           TransactionRepository transactionRepository, AuthDetails authDetails, MailService mailService) {

        this.userRepository = userRepository;
        this.subjectsRepository = subjectsRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.walletRepository = walletRepository;
        this.notificationRepository = notificationRepository;
        this.authDetails = authDetails;
        this.transactionRepository = transactionRepository;
        this.mailService = mailService;
    }

    @Override
    public ResponseAPI<TeacherDto> TeacherSignUp(TeacherDto teacherDto) {

        Optional<Teacher> user = teacherRepository.findTeacherByEmail(teacherDto.getEmail());
        Wallet wallet = new Wallet();

        if (user.isEmpty()) {

            passwordEncoder = new BCryptPasswordEncoder();
            Teacher teacher = new Teacher();

            teacher.setName(teacherDto.getName());
            teacher.setEmail(teacherDto.getEmail());
            teacher.setPassword(passwordEncoder.encode(teacherDto.getPassword()));
            teacher.setSchool(teacherDto.getSchool());
            teacher.setRole(Roles.TEACHER);
            teacher.setYearsOfTeaching(teacherDto.getYearsOfTeaching());
            teacher.setPeriodOfTeaching(teacherDto.getPeriodOfTeaching());
            teacher.setPosition(teacherDto.getPosition());
            teacher.setSchoolType(teacherDto.getSchoolType());
            teacher.setProvider(Provider.LOCAL);


            wallet.setBalance(BigDecimal.valueOf(0.0));

            walletRepository.save(wallet);
            teacher.setWallet(wallet);


            teacherRepository.save(teacher);
//            User user1 = userRepository.save(teacher);



            for(String subjectTitle: teacherDto.getSubjectsList()) {
                subjectsRepository.save(new Subjects(subjectTitle, teacher.getId()));

            }

            return new ResponseAPI<>("User Registration successful", LocalDateTime.now(), teacherDto);

        } else {

            throw new EmailAlreadyExistsException("Email Already Exists");
        }

    }

    @Override
    public ResponseAPI<StudentDto> StudentSignUp(StudentDto studentDto) {


        Optional<Student> user = studentRepository.findStudentByEmail(studentDto.getEmail());

        Wallet wallet = new Wallet();

        if (user.isEmpty()) {

            passwordEncoder = new BCryptPasswordEncoder();
            Student student = new Student();

            student.setName(studentDto.getName());
            student.setEmail(studentDto.getEmail());
            student.setPassword(passwordEncoder.encode(studentDto.getPassword()));
            student.setSchool(studentDto.getSchool());
            student.setRole(Roles.STUDENT);
            student.setProvider(Provider.LOCAL);

            wallet.setBalance(BigDecimal.valueOf(0.0));

            walletRepository.save(wallet);
            student.setWallet(wallet);


            studentRepository.save(student);



            return new ResponseAPI<>("User Registration successful", LocalDateTime.now(), studentDto);
        } else {

            throw new EmailAlreadyExistsException("Email Already Exists");
        }
    }

    @Override
    public ResponseAPI<TeacherDto> viewTeacher(long id) {

        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(teacher.getId());
        teacherDto.setRole(teacher.getRole());
        teacherDto.setName(teacher.getName());
        teacherDto.setAbout(teacher.getAbout());
        teacherDto.setPosition(teacher.getPosition());
        teacherDto.setEmail(teacher.getEmail());
        teacherDto.setSchool(teacher.getSchool());
        teacherDto.setYearsOfTeaching(teacher.getYearsOfTeaching())
        ;
        teacherDto.setSchoolType(teacher.getSchoolType());

        List<String> listOfSubjects = new ArrayList<>();

        for (Subjects sub: teacher.getSubjectsList()) {
            listOfSubjects.add(sub.getTitle());
        }

        teacherDto.setSubjectsList(listOfSubjects);


        return new ResponseAPI<>("success", LocalDateTime.now(), teacherDto);
    }

    @Override
    public ResponseAPI<List<TeacherDto>> searchForTeacher(String name) {

        List<Teacher> teacherList =  teacherRepository.findAllByName(name);

        teacherRepository.findAll();

        List<TeacherDto> teacherDtos = new ArrayList<>();

        for (Teacher teacher: teacherList) {
            TeacherDto teacherDto = new TeacherDto();
            teacherDto.setName(teacher.getName());
            teacherDto.setEmail(teacher.getEmail());
            teacherDto.setSchool(teacher.getSchool());
            teacherDto.setYearsOfTeaching(teacher.getYearsOfTeaching());
            teacherDto.setSchoolType(teacher.getSchoolType());

            List<String> listOfSubjectList = new ArrayList<>();

            for(Subjects subject: teacher.getSubjectsList()) {
                listOfSubjectList.add(subject.getTitle());
            }
            teacherDto.setSubjectsList(listOfSubjectList);

            teacherDtos.add(teacherDto);
        }

        return new ResponseAPI<>("message", LocalDateTime.now(), teacherDtos);
    }


    public List<TeacherResponseDto> retrieveTeacher(int page, int size) {
        List<Teacher> users;
        Pageable firstPageWithFiveElements = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Teacher> userPage;

        userPage = teacherRepository.findAll(firstPageWithFiveElements);

        users = userPage.getContent();


        return convertTeachersToResponseDto(users);
    }



    public String processOAuthUser(UserDto userDto, Authentication authentication) {
        Optional<User> existUser = userRepository.findUserByEmail(userDto.getEmail());
        if(existUser.isEmpty()) {
            User newUser = new User();
            newUser.setName(userDto.getName());
            newUser.setEmail(userDto.getEmail());
            newUser.setProvider(Provider.GOOGLE);
            newUser.setPassword(new PasswordConfig()
                    .passwordEncoder().encode(userDto.getName()));// set user's name as default password
            userRepository.save(newUser);
        }
        return jwtTokenProvider.generateToken(authentication);
    }

    public User getTeacherByEmail(String email) {
        Optional<Teacher> user = teacherRepository.findTeacherByEmail(email);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    public User getStudentByEmail(String email) {
        Optional<Student> user = studentRepository.findStudentByEmail(email);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    public String getUserRole(String email) {
        User user = getUserByEmail(email);

        String userRole = String.valueOf(user.getRole());
        return userRole;
    }

    @Override
    public ResponseAPI<BigDecimal> userWalletBalance(Long id)  {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        BigDecimal walletBallance = user.getWallet().getBalance();

        return new ResponseAPI<>("success", LocalDateTime.now(), walletBallance);
    }
    @Override
    public ResponseAPI<BigDecimal> userWalletBalance()  {
        User user = userRepository.findUserByEmail(authDetails.getAuthorizedUserEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        BigDecimal walletBalance = user.getWallet().getBalance();
        BigDecimal totalMoneySent = user.getTransactionList().stream().filter(transaction -> transaction.getTransactionType().equals(DEBIT)).map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(totalMoneySent);
        return new ResponseAPI<>("success", LocalDateTime.now(), walletBalance);
    }

    @Override
    public ResponseAPI<BigDecimal> totalMoneySent()  {
        User user = userRepository.findUserByEmail(authDetails.getAuthorizedUserEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        BigDecimal totalMoneySent = user.getTransactionList().stream().filter(transaction -> transaction.getTransactionType().equals(DEBIT)).map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(totalMoneySent);
        return new ResponseAPI<>("success", LocalDateTime.now(), totalMoneySent);
    }


    public  List<TeacherResponseDto> retrieveAllTeachersBySchool(String school, int pageNo, int pageSize){

    Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("name").ascending());
    Page<Teacher> teachers = teacherRepository.findAllBySchool(school, pageable);

    List<Teacher> teacherList = teachers.getContent();

        return  convertTeachersToResponseDto(teacherList);

    }

    public List<TeacherResponseDto> convertTeachersToResponseDto(List<Teacher> teachers){

        List<TeacherResponseDto> teacherResponseDtoList = new ArrayList<>();

        teachers.forEach(teacher -> {
            TeacherMapper mapper = new TeacherMapper();
            TeacherResponseDto teacherResponseDto =  mapper.teacherEntityToTeacherResponseDtoMapper(teacher);
            teacherResponseDtoList.add(teacherResponseDto);

        });

        return teacherResponseDtoList;
    }

    @Override
    public Page<Notification> retrieveNotifications(Long userId) {
        Pageable pageable = PageRequest.of(0, 5);
        return notificationRepository.findAllByUser_Id(userId, pageable);
    }

    @Override
    public ResponseEntity<Object> getExistingStudentProfile(Long studentId) {
        Optional<User> student = userRepository.findById(studentId);
        if(student.isPresent()) {
            User student1 = student.get();
            StudentEditDto studentEditDto = new StudentEditDto();
            studentEditDto.setName(student1.getName());
            studentEditDto.setEmail(student1.getEmail());
            studentEditDto.setSchool(student1.getSchool());
            return  new ResponseEntity<>(studentEditDto, OK);

        }else {
            return  new ResponseEntity<>("student not found", NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Object> getExistingTeacherProfile(Long teacherId) {

        Optional<User> teacher = userRepository.findById(teacherId);
        if(teacher.isPresent()){
            User teacher1 =teacher.get();
            TeacherEditDto teacherEditDto = new TeacherEditDto();
            teacherEditDto.setName(teacher1.getName());
            teacherEditDto.setEmail(teacher1.getEmail());
            teacherEditDto.setSchool(teacher1.getSchool());
            teacherEditDto.setAbout(teacher1.getAbout());
            teacherEditDto.setYearsOfTeaching(((Teacher) teacher1).getYearsOfTeaching());
            for(Subjects subject: ((Teacher) teacher1).getSubjectsList()){
                teacherEditDto.getSubjectsList().add(subject.getTitle());
            }
            teacherEditDto.setSchoolType(((Teacher) teacher1).getSchoolType());
            return new ResponseEntity<>(teacherEditDto, OK);
        }else{
            return new ResponseEntity<>("teacher not found", NOT_FOUND);
        }

    }


    @Override
    public ResponseEntity<Object> updateStudent(StudentEditDto studentEditDto, Long studentId) {
        Optional<User> student = userRepository.findById(studentId);
        if (studentEditDto == null) {
            return new ResponseEntity<>("Student information not found!", NOT_FOUND);
        } else if (student.isEmpty()) {
            return new ResponseEntity<>("Student not found!", NOT_FOUND);
        } else {
            Student student1 = (Student) student.get();
            student1.setName(studentEditDto.getName());
            student1.setEmail(studentEditDto.getEmail());
            student1.setSchool(studentEditDto.getSchool());
            userRepository.save(student1);
            return new ResponseEntity<>("Student's profile updated successfully", OK);
        }
    }

    @Override
    public ResponseEntity<Object> updateTeacher(TeacherEditDto teacherEditDto, Long teacherId) {
        Optional<Teacher> teacher =  teacherRepository.findById(teacherId);
        if (teacherEditDto == null) {
            return new ResponseEntity<>("Teacher's information not found", NOT_FOUND);
        } else if (teacher.isEmpty()) {
            return new ResponseEntity<>("Teacher not found!", NOT_FOUND);
        } else {
            passwordEncoder = new BCryptPasswordEncoder();
            Teacher teacher1 = teacher.get();
            teacher1.setName(teacherEditDto.getName());
            teacher1.setEmail(teacherEditDto.getEmail());
            teacher1.setPassword(passwordEncoder.encode(teacherEditDto.getPassword()));
            teacher1.setSchool(teacherEditDto.getSchool());

            for(String subjectTitle: teacherEditDto.getSubjectsList()) {
                subjectsRepository.save(new Subjects(subjectTitle, teacher1.getId()));
            }
            teacher1.setYearsOfTeaching(teacherEditDto.getYearsOfTeaching());
            teacher1.setSchoolType(teacherEditDto.getSchoolType());
            teacherRepository.save(teacher1);
            return new ResponseEntity<>("Teacher's profile edited successfully", OK);
        }
    }

    @Override
    public ResponseEntity<Object> teacherAppreciatesStudentForReward(Long teacherId, Long studentId)
            throws MessagingException{
        Optional<User> teacher = userRepository.findById(teacherId);
        Optional<User> student = userRepository.findById(studentId);
        if (teacher.isEmpty()) {
            return  new ResponseEntity<>("Teacher not found", NOT_FOUND);
        } else if (student.isEmpty()) {
            return  new ResponseEntity<>("Student not found", NOT_FOUND);
        } else {
            Teacher teacher1 = (Teacher) teacher.get();
            Student student1 = (Student) student.get();
            Notification notification = new Notification();
            notification.setUser(student1);
            notification.setMessageBody("Your teacher, " + teacher1.getName() + " appreciates you for your reward sent");
            AppreciateStudentDto appreciateStudentDto = new AppreciateStudentDto();
            appreciateStudentDto.setMessageBody(notification.getMessageBody());
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
            String formattedDate = myDateObj.format(myFormatObj);
            appreciateStudentDto.setCreatedAt(formattedDate);
            notificationRepository.save(notification);
            String email = student1.getEmail();
            String mailSubject = "Appreciation Notification";
            String emailBody = "Hello " + student1.getName() +","+ "\n"
                    +"Your teacher, "+ teacher1.getName() +  " appreciates you for your reward sent!" + "\n";

            mailService.sendEmail(email, mailSubject, emailBody);
            return  new ResponseEntity<>(appreciateStudentDto, OK);
        }
    }
}


