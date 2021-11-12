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
    @DisplayName("when create topping is called and the inputs are valid, topping should be created and saved")
    public void createTopping_validInput_createdTopping() {
        Topping topping = toppingService.createTopping("Lemon", 2D);

        assertThat(topping.getName()).isEqualTo("Lemon");
        assertThat(topping.getAmount()).isEqualTo(2D);
        Mockito.verify(toppingRepository, times(1)).save(topping);
    }

    @Test
    @DisplayName("when update topping is called and the inputs are valid, topping should be updated")
    public void updateTopping_validInput_updatedTopping() {
        Topping topping = this.createSampleTopping();
        Mockito.doReturn(Optional.of(topping)).when(toppingRepository).findById(10L);
        toppingService.updateTopping(10L, "Hazelnut syrup", 3D);

        assertThat(topping.getName()).isEqualTo("Hazelnut syrup");
        assertThat(topping.getAmount()).isEqualTo(3D);
    }

    @Test
    @DisplayName("when updating not existing topping, then throw topping not found exception")
    public void updateTopping_toppingNoExist_ThrowException() {
        Mockito.doReturn(Optional.empty()).when(toppingRepository).findById(10L);
        assertThrows(ToppingNotFoundException.class, () -> toppingService.updateTopping(10L, "Lemon", 2D));
    }


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
