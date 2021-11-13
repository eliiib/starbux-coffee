package com.starbux.coffee.service;


import com.starbux.coffee.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(OrderServiceImpl.class)
public class ReportServiceImplTest {

}
