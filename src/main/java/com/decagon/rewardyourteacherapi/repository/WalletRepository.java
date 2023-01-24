package com.decagon.rewardyourteacherapi.repository;

import com.decagon.rewardyourteacherapi.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




/**
 * @author ifeoluwa on 19/09/2022
 * @project
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findByWalletAddress(String walletAddress);

    Wallet findWalletByUserId(Long userId);

}

