package com.starbux.coffee.service;

import com.starbux.coffee.domain.Topping;
import com.starbux.coffee.exception.ToppingNotFoundException;
import com.starbux.coffee.repository.ToppingRepository;
import com.starbux.coffee.service.impl.ToppingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@Import(ToppingServiceImpl.class)
public class ToppingServiceImplTest {

    @Autowired
    private ToppingServiceImpl toppingService;

    @MockBean
    private ToppingRepository toppingRepository;

    @Test
    @DisplayName("when delete topping is called and the id is valid, the delete flag should set true")
    public void deleteTopping_validId_setDeleteFlag() {
        Topping topping = this.createSampleTopping();
        Mockito.doReturn(Optional.of(topping)).when(toppingRepository).findById(12L);
        toppingService.deleteTopping(12L);

        assertThat(topping.getIsDeleted()).isTrue();
        Mockito.verify(toppingRepository, times(1)).save(topping);
    }


    @Test
    @DisplayName("Test deleting not existing topping, then throw topping not found exception")
    public void testDeleteTopping_toppingNoExist_ThrowException() {
        Mockito.doReturn(Optional.empty()).when(toppingRepository).findById(13L);
        assertThrows(ToppingNotFoundException.class, () -> toppingService.deleteTopping(13L));
    }

    private Topping createSampleTopping() {
        return Topping.builder()
                .id(12L)
                .name("Milk")
                .amount(2D)
                .createDate(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }
}
