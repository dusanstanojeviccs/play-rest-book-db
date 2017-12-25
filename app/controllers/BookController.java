package controllers;

import com.google.inject.Inject;
import play.db.Database;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class BookController extends Controller {

    @Inject public BookRepository bookRepository;
    @Inject Database database;

    public Result findAll() {
        return database.withConnection(conn -> {
            return ok(Json.toJson(bookRepository.findAll())).as("application/json");
        });
    }

    public Result findById(int id) {
        return database.withConnection(conn -> {
            return ok(Json.toJson(bookRepository.findById(id))).as("application/json");
        });
    }

    public Result create() {
        return database.withConnection(conn -> {
            Book bookRequest = Json.fromJson(request().body().asJson(), Book.class);

            bookRepository.add(bookRequest);

            return ok(Json.toJson(bookRequest)).as("application/json");
        });
    }

    public Result update(int id) {
        return database.withConnection(conn -> {
            Book bookRequest = Json.fromJson(request().body().asJson(), Book.class);

            bookRequest.setId(id);

            bookRepository.update(bookRequest);

            return ok(Json.toJson(bookRequest)).as("application/json");
        });
    }

    public Result delete(int id) {
        return database.withConnection(conn -> {
            bookRepository.delete(id);
            return ok("{}").as("application/json");
        });
    }
}