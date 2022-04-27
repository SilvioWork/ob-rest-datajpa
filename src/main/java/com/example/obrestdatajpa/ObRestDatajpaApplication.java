package com.example.obrestdatajpa;

import com.example.obrestdatajpa.entity.Book;
import com.example.obrestdatajpa.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class ObRestDatajpaApplication {



	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ObRestDatajpaApplication.class, args);

		BookRepository bookRepository = context.getBean(BookRepository.class);
		Book book = new Book(null, "Lord of the Ring", "Silvio", 545, 102.45, LocalDate.of(2000, 12, 25),true);
		Book book1 = new Book(null, "Hobbit", "Silan", 621, 140.45, LocalDate.of(2002, 12, 25),true);
        bookRepository.save(book);
        bookRepository.save(book1);

		}

}
