package com.geraldsaccount.killinary.mappers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.geraldsaccount.killinary.model.User;
import com.geraldsaccount.killinary.model.dinner.CharacterAssignment;
import com.geraldsaccount.killinary.model.dinner.Dinner;
import com.geraldsaccount.killinary.model.dinner.DinnerStatus;
import com.geraldsaccount.killinary.model.dto.output.detail.HostInfoDto;
import com.geraldsaccount.killinary.model.dto.output.detail.PrivateInfoDto;
import com.geraldsaccount.killinary.model.dto.output.dinner.PreDinnerInfoDto;
import com.geraldsaccount.killinary.model.mystery.Crime;
import com.geraldsaccount.killinary.model.mystery.Mystery;
import com.geraldsaccount.killinary.model.mystery.Story;

@SuppressWarnings("unused")
class DinnerMapperTest {
    private UserMapper userMapper;
    private CharacterMapper characterMapper;
    private DinnerMapper dinnerMapper;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        characterMapper = mock(CharacterMapper.class);
        dinnerMapper = new DinnerMapper(userMapper, characterMapper);
    }

    @Test
    void getPreDinnerInfo_returnsDto_whenCalled() {
        Dinner dinner = mock(Dinner.class);
        Mystery mystery = mock(Mystery.class);
        Story story = mock(Story.class);
        User host = mock(User.class);
        Set<CharacterAssignment> assignments = new HashSet<>();
        when(dinner.getMystery()).thenReturn(mystery);
        when(mystery.getStory()).thenReturn(story);
        when(dinner.getCharacterAssignments()).thenReturn(assignments);
        when(dinner.getHost()).thenReturn(host);
        UUID dinnerId = UUID.randomUUID();
        when(dinner.getId()).thenReturn(dinnerId);
        LocalDateTime date = LocalDateTime.now();
        when(dinner.getDate()).thenReturn(date);
        when(story.getTitle()).thenReturn("Test Story");
        when(story.getBannerImage()).thenReturn(null);
        when(story.getSetting()).thenReturn("Test Setting");
        when(story.getRules()).thenReturn("Test Rules");
        when(userMapper.asDTO(any())).thenReturn(null);
        when(characterMapper.asDetailDTO(any())).thenReturn(null);

        PreDinnerInfoDto dto = dinnerMapper.getPreDinnerInfo(dinner);
        assertThat(dto).isNotNull();
        assertThat(dto.storyTitle()).isEqualTo("Test Story");
        assertThat(dto.setting()).isEqualTo("Test Setting");
        assertThat(dto.rules()).isEqualTo("Test Rules");
        assertThat(dto.dateTime()).isEqualTo(date);
    }

    @Test
    void getPrivateInfoForUserDto_returnsNull_whenNotAssigned() {
        Dinner dinner = mock(Dinner.class);
        User user = mock(User.class);
        when(dinner.getCharacterAssignments()).thenReturn(Collections.emptySet());
        assertNull(dinnerMapper.getPrivateInfoForUser(user, dinner));
    }

    @Test
    void getPrivateInfoForCharacterDto_returnsDto_whenCalled() {
        com.geraldsaccount.killinary.model.mystery.Character character = mock(
                com.geraldsaccount.killinary.model.mystery.Character.class);
        Dinner dinner = mock(Dinner.class);
        when(dinner.getStatus()).thenReturn(DinnerStatus.CREATED);
        UUID charId = UUID.randomUUID();
        when(character.getId()).thenReturn(charId);
        when(character.getPrivateDescription()).thenReturn("desc");
        when(character.getRelationships()).thenReturn(Collections.emptyMap());
        PrivateInfoDto dto = dinnerMapper.getPrivateInfoForCharacter(character,
                dinner);
        assertThat(dto).isNotNull();
        assertThat(dto.characterId()).isEqualTo(charId);
        assertThat(dto.characterDescription()).isEqualTo("desc");
    }

    @Test
    void getConclusion_returnsNull_whenNotConcluded() {
        Dinner dinner = mock(Dinner.class);
        Mystery mystery = mock(Mystery.class);
        Crime crime = mock(Crime.class);
        when(dinner.getMystery()).thenReturn(mystery);
        when(mystery.getCrime()).thenReturn(crime);
        when(dinner.getStatus()).thenReturn(DinnerStatus.CREATED);
        assertThat(dinnerMapper.getConclusion(dinner)).isNull();
    }

    @Test
    void getHostInfo_returnsDto_whenCalled() {
        Dinner dinner = mock(Dinner.class);
        Mystery mystery = mock(Mystery.class);
        Story story = mock(Story.class);
        when(dinner.getMystery()).thenReturn(mystery);
        when(mystery.getStory()).thenReturn(story);
        when(dinner.getCharacterAssignments()).thenReturn(Collections.emptySet());
        when(dinner.getStatus()).thenReturn(DinnerStatus.CREATED);
        when(story.getBriefing()).thenReturn("brief");
        HostInfoDto dto = dinnerMapper.getHostInfo(dinner);
        assertThat(dto).isNotNull();
        assertThat(dto.briefing()).isEqualTo("brief");
    }
}
