package servlets.servlets.tasks;

import app.models.TaskModel;
import app.entities.Task;
import app.utils.Logger;
import app.utils.JsonData;
import app.utils.Validator;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import java.io.IOException;


@WebServlet(name = "Task", urlPatterns = { "/api/tasks/*", "/tasks/" })
public class TaskApi extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String requestPath = request.getPathInfo();
        String id = requestPath.substring(requestPath.lastIndexOf("/") + 1);
        String action = requestPath.substring(1, requestPath.lastIndexOf("/"));
        if (Validator.isValidId(id) == false) {
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid task id: " + id + "\"}");
            return;
        }

        if (action.equals("delete")) {
            Logger.log("TaskApi doPost: delete", id);
            new TaskModel().delete(Integer.parseInt(id));
            response.getWriter().write("{\"status\": \"success\", \"message\": \"Task deleted\"}");
        } else if (action.equals("update")) {
            JsonData data = (JsonData) request.getAttribute("data");
            String name = data.getString("name");
            String description = data.getString("description");
            String status = data.getString("status");
            Task task = new TaskModel().getById(Integer.parseInt(id));
            task.setName(name);
            task.setDescription(description);
            task.setStatus(status);
            new TaskModel().update(task);

            response.getWriter().write("{\"status\": \"success\", \"message\": \"Task updated\"}");
        } else {
            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid action " + action + "\"}");
        }
    }
}
