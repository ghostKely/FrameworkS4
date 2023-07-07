/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import etu1886.frameworki.Mapping;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import etu1886.frameworki.Utilitaire;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import traitment.Fonction;
import traitment.ModelView;

/**
 *
 * @author ITU
 */
public class Frontservlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
    
    */
    
   HashMap<String,Mapping> MappingUrls;

   public void init() throws ServletException{
        String url = getServletContext().getRealPath("/");   
        Fonction fonction=new Fonction();
       try {    
           MappingUrls=fonction.listeHashMapAllClass(url);
       } catch (ClassNotFoundException ex) {
           Logger.getLogger(Frontservlet.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Utilitaire u=new Utilitaire();
            String url=request.getPathInfo();
            String annotation=u.getAnnotation(url);
            Fonction fonction=new Fonction();
           
            try{
                Mapping mapping=fonction.getMapping(annotation, MappingUrls);
                ModelView invomethode=fonction.invocationMethode(annotation, MappingUrls);
                out.println("methode: /"+ invomethode.getView());
                response.sendRedirect(request.getContextPath()+"/"+invomethode.getView());
            }catch(Exception ex){
                out.print(ex);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       try {
           processRequest(request, response);
       } catch (Exception ex) {
           Logger.getLogger(Frontservlet.class.getName()).log(Level.SEVERE, null, ex);
       }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       try {
           processRequest(request, response);
       } catch (Exception ex) {
           Logger.getLogger(Frontservlet.class.getName()).log(Level.SEVERE, null, ex);
       }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
