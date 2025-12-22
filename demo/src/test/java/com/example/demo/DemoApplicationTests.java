package com.example.demo;

import com.example.demo.dto.BoardDTO;
import com.example.demo.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private BoardService boardService;

	@Test
	void contextLoads() {
		for(int i=0; i<500; i++){

			BoardDTO boardDTO = BoardDTO.builder()
					.title("test Title " + (int)(Math.random()*100))
					.writer("tester " + (int)(Math.random()*100)+"@test.com")
					.content("test Content" + i)
					.build();
			boardService.insert(boardDTO);
		}
	}

}
