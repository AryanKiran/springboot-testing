package com.codingshuttle.TestingApp;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class TestingAppApplicationTests {

	@BeforeEach//runs before every method call.
	void setUp(){
		log.info("Starting the method,setting up things");
	}
	@AfterEach//runs  after every method call.
	void tearDown(){
		log.info("closing the resources after run");
	}
	@Test
	void ContextLoad(){
		log.info("Context Load");
	}

	@Test
	@DisplayName("Hello world program")
	void HellWorld(){
		log.info("hello warahn");
	}
	@BeforeAll
    static void setUpOnce(){
		log.info("runs as the starting the method,before any method runs");
	}

	@AfterAll
	static void tearDownOnce(){
		log.info("runs as the ending the method,after any method runs");
	}

	@Test
	void testNumberOne(){
		int a = 5;
		int b = 6;
		int result = a+b;

//		Assertions.assertEquals(11,result);

//		assertThat(result)
//				.isEqualTo(10)
//				.isCloseTo(11, Offset.offset(1));

		assertThat("value").endsWith("value");
	}

	@Test
	void testDivideTwoNumbers_WhenDenominatorIsZero(){
		int a  = 4;
		int b = 0;
		assertThatThrownBy(()-> divideTwoNumbers(a,b))
				.isInstanceOf(ArithmeticException.class)
				.hasMessage("tried to divide by zero");
	}

	 void divideTwoNumbers(int a, int b) {
			try{
				int ans = a/b;

			}catch (ArithmeticException e){
				throw new ArithmeticException("tried to divide by zero");
			}
	}
}
















