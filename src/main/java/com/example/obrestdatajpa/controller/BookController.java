package com.example.obrestdatajpa.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.example.obrestdatajpa.entity.Book;
import com.example.obrestdatajpa.repository.BookRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/books")
    @ApiOperation("List all books")
    public List<Book> listAll(){
        return bookRepository.findAll();
    }

    @GetMapping("/books/{id}")
    @ApiOperation("Return a Book by primary id Long")
    public ResponseEntity<Book> showBook(@ApiParam("Primary id Long of Book") @PathVariable Long id){
        Optional<Book> optionalBook = bookRepository.findById(id);

        if(optionalBook.isPresent()){
            return ResponseEntity.ok(optionalBook.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/books")
    @ApiOperation("Create a book")
    public ResponseEntity<?> createBook(@RequestBody Book book, @RequestHeader HttpHeaders httpHeaders){
        HashMap<String, Object> response = new HashMap<>();

        try {
            bookRepository.save(book);
            response.put("mensaje", "The Book is created -" + httpHeaders.get("User-Agent") );
        }catch (DataAccessException e){
             response.put("mensaje", "Error creating Book");
             response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
             return new ResponseEntity<HashMap<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/books/{id}")
    @ApiOperation("List all books")
    public ResponseEntity<?> updateBook(@RequestBody Book book, BindingResult result,@ApiParam("Primary id Long of Book") @PathVariable Long id){
        Optional<Book> actulBook = bookRepository.findById(id);
        System.out.println("pase");
        HashMap<String, Object> response = new HashMap<>();

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> "The Field " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if( actulBook.isEmpty()){
            System.out.println("entre");
            response.put("mensaje" ,"The Book with id: " + id + " not fount");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        actulBook.get().setAuthor(book.getAuthor());
        actulBook.get().setPages(book.getPages());
        actulBook.get().setOnline(book.isOnline());
        actulBook.get().setTitle(book.getTitle());
        actulBook.get().setPrice(book.getPrice());
        actulBook.get().setReleaseDate(book.getReleaseDate());

        try {
            bookRepository.save(book);
            response.put("mensaje", "The Book is Updated" );
        }catch (DataAccessException e){
            response.put("mensaje", "Error updating Book");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/books/{id}")
    @ApiOperation("Delete book by primary id Long")
    public ResponseEntity<?> deleteBook(@ApiParam("Primary id Long of Book") @PathVariable Long id){
        Optional<Book> book = bookRepository.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (!book.isEmpty()){
            try {
                bookRepository.delete(book.get());

            }catch (DataAccessException e){
                return ResponseEntity.notFound().build();
            }
        }else{
            response.put("message", "The book with id: " + id + " not Exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("mensaje", "The Book has been deleted conrrectry");
        return ResponseEntity.ok(response);
    }
}
