package com.irostub.domain.entity.standard;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FridayLunchAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @ManyToOne
    private FridayLunch fridayLunch;

    public static FridayLunchAccount create(Account account, FridayLunch fridayLunch){
        FridayLunchAccount fridayLunchAccount = new FridayLunchAccount();
        fridayLunchAccount.account = account;
        fridayLunchAccount.fridayLunch = fridayLunch;
        return fridayLunchAccount;
    }
}
