package com.irostub.domain.entity.standard;

import com.irostub.domain.entity.BaseUserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FridayLunch extends BaseUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean alert;
    @OneToOne
    private ChatGroup chatGroup;

    @OneToMany(mappedBy = "fridayLunch")
    private List<FridayLunchAccount> fridayLunchAccounts = new ArrayList<>();

    public static FridayLunch create(ChatGroup chatGroup) {
        FridayLunch fridayLunch = new FridayLunch();
        fridayLunch.alert = false;
        fridayLunch.chatGroup = chatGroup;
        return fridayLunch;
    }
    public boolean isAlertOn(){
        return alert;
    }

    public void alertOff(){
        this.alert = false;
    }

    public void alertOn(){
        this.alert = true;
    }
}
