package com.irostub.whaple.bot.fridaylunch;

import com.irostub.whaple.bot.account.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FridayLunchRepository extends JpaRepository<FridayLunch, Integer> {

    @Query("select f from FridayLunch f join f.chatGroup cg on cg.chatGroupId = :chatGroupId where f.alert=true")
    Optional<FridayLunch> findByChatGroupId(Long chatGroupId);
    Optional<FridayLunch> findByChatGroup(ChatGroup chatGroup);

    List<FridayLunch> findByAlert(Boolean alert);
}
