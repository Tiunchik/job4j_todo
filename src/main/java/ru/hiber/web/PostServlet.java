/**
 * Package ru.hiber.web for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.hiber.logic.ToDoLogic;
import ru.hiber.model.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class PostServlet -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 05.04.2020
 */
@WebServlet(urlPatterns = {"/post"})
public class PostServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(PostServlet.class.getName());

    private static final ToDoLogic LOGIC = ToDoLogic.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder fullLine = new StringBuilder();
        try (BufferedReader read = req.getReader();
             PrintWriter writer = resp.getWriter()) {
            String oneline;
            while ((oneline = read.readLine()) != null) {
                fullLine.append(oneline);
            }
            JSONObject json = (JSONObject) new JSONParser().parse(fullLine.toString());
            String key = (String) json.get("action");
            if (key.equalsIgnoreCase("getAll")) {
                List<Item> listItems = LOGIC.getAll()
                        .stream()
                        .sorted(Comparator.comparingInt(Item::getId))
                        .collect(Collectors.toList());
                if (!listItems.isEmpty()) {
                    String postAnswer = new Gson().toJson(listItems);
                    writer.write(postAnswer);
                    writer.flush();
                }
                resp.setStatus(200);
            }
            if (key.equalsIgnoreCase("save")) {
                Item item = new Item();
                item.setDescription((String) json.get("description"));
                item.setCreated(new Timestamp((Long) json.get("created")));
                item.setDone(false);
                LOGIC.save(item);
            }
            if (key.equalsIgnoreCase("update")) {
                Item item = new Item();
                item.setId(Integer.parseInt((String) json.get("id")));
                item = LOGIC.get(item);
                if (item.isDone()) {
                    item.setDone(false);
                } else {
                    item.setDone(true);
                }
                LOGIC.update(item);
                resp.setStatus(200);
            }
        } catch (ParseException e) {
            LOG.error("JSON parse exception", e);
            resp.setStatus(500);
        }
    }
}
