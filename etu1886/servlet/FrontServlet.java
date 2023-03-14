package etu1886.servlet;
import java.io.*;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import etu1886.mapping.Mapping;
import etu1886.utilitaire.Function;

public class FrontServlet extends HttpServlet{
    HashMap<String, Mapping> mappingurls;
    public void ProcessRequest(HttpServletRequest req , HttpServletResponse resp) throws IOException, ClassNotFoundException {
        PrintWriter printer = resp.getWriter();
        Function function = new Function();
        String[] datas = function.dataurl(req, resp);

        function.getmappingbyurl(req,resp,"models");
    }

    public void doGet(HttpServletRequest req , HttpServletResponse resp) throws IOException {
        try {
            this.ProcessRequest(req,resp);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest req , HttpServletResponse resp) throws IOException {
        try {
            this.ProcessRequest(req,resp);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
