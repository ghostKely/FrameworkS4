package etu1886.utilitaire;

import etu1886.mapping.Mapping;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Object.*;
import java.awt.*;
import java.lang.Class;
import java.lang.reflect.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.*;

public class Function {


    public static List<String> getInfoURL(HttpServletRequest req) {
        return Arrays.asList(req.getServletPath().split("/"));
    }

    public static List<Class> getClasses(String packageName) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();

        URL resource;
        URI uri;

        while (resources.hasMoreElements()) {
            resource = resources.nextElement();
            uri = new URI(resource.toString());
            dirs.add(new File(uri.getPath()));
        }
        List<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }


    private static List<Class> findClasses(File directory, String packageName) throws Exception {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null)
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classes.add(Class.forName(className));
                }
            }
        else
            throw new Exception("Il n'y a pas de fichiers dans : "+directory);
        return classes;
    }

    // initPram : Lister des packages à scanner (dans web.xml)
    // Nouvel argument : List<String> listPackages
    // ET on a ensuite la liste de toutes les classes dans tous les packages
    public static HashMap<String, Mapping> initHashMap(String path) throws Exception {

        // Lasa boucler-na par liste
        List<Class> listClass = Utilitaire.getClasses(path);

        Method[] meths;

        HashMap<String, Mapping> mappingUrls = new HashMap<>();
        Annotation annot;
        String valueUrl = null;
        Method annotMeth;
        Mapping mapping;

        for (Class c : listClass) {
            if (c.isAnnotationPresent(Model.class)) {
                meths = c.getDeclaredMethods();
                for (Method m : meths) {
                    if (m.isAnnotationPresent(URLMapping.class)) {

                        annot = m.getAnnotation(URLMapping.class);
                        annotMeth = annot.annotationType().getDeclaredMethod("valeur");
                        valueUrl = annotMeth.invoke(annot).toString();

                        mapping = new Mapping(c.getName(), m.getName());
                        mappingUrls.put(valueUrl, mapping);

                    }
                }
            }
        }
        return mappingUrls;
    }

    public Mapping getmappingbyurl(HttpServletRequest req , HttpServletResponse resp, String packagename) throws IOException, ClassNotFoundException {
        Mapping mapping = new Mapping("unknown","unknown");
        String[] datainurl = this.dataurl(req,resp);
        String verifclassname = this.verifclass(req,resp,datainurl[0],packagename);
        String verifmethodname = this.verifclass(req,resp,datainurl[1],packagename);

        mapping.setClassname(verifclassname);
        mapping.setMethodname(verifmethodname);

        return mapping;
    }

    public String verifmethod(HttpServletRequest req , HttpServletResponse resp, String methodname, String packagename) throws IOException, ClassNotFoundException {
        String verif = "Tsy fantatra";

        AllClass call = new AllClass();                                     //miantso fonction maka class rehetra
        List<Class> allclasses = call.getClasses(packagename);

        for (Class oneclass : allclasses) {
            Method[] fonction = oneclass.getDeclaredMethods();
            for (Method onefonction : fonction) {
                String onefcontionname = onefonction.getName();
                if (onefcontionname.equalsIgnoreCase(methodname)) {
                    verif = onefcontionname;
                    break;
                }
            }
        }

        return verif;
    }

    public String verifclass(HttpServletRequest req , HttpServletResponse resp, String classname, String packagename) throws IOException, ClassNotFoundException {
        String verif = "Tsy fantatra";

        AllClass call = new AllClass();                                     //miantso fonction maka class rehetra
        List<Class> allclasses = call.getClasses(packagename);

        for (Class oneclass : allclasses) {
            String oneclassname = oneclass.getSimpleName();
            if (oneclassname.equalsIgnoreCase(classname)) {
                verif = oneclassname;
                break;
            }
        }

        return verif;
    }

/*==================fonction pour spliter les donnees de la data.txt par le slash==================

        BUT : avoir data splitee par le slash cad les lignes du tableau
        INTERET : avoir la liste des datas
            ex :
                avant split : id,nom,prenom/1,burh,well_done
                apres split :
                            id,nom,prenom
                            1,burh,well_done
*/
    public String[] splitbyslash(String data) {     //donnees en argument
        String[] datasplited = data.split("/");     //splitage par le slash
        return datasplited;                         //retourne la requete sous frome de tableau
    }

    public String[] dataurl(HttpServletRequest req , HttpServletResponse resp) throws IOException {
        PrintWriter printer = resp.getWriter();
        String url = req.getServletPath().replaceFirst("/","");
        printer.println("URL PATH :"+url);
        String[] datas = this.splitbyslash(url);

        for (String string : datas) {
            printer.println("value :"+string+"\n");
        }

        return datas;
    }

/*==============fonction show field's annotation==============
        BUT : afficher annotation des fonctions dans une classe
        INTERET : manao sysout auto 
*/
    public void showmethodannotation(Class objclass) {       //nom du class en arguement
        Method[] fonction = objclass.getDeclaredMethods();    //maka anle fonction rehetra
        if (fonction.length == 0) { System.out.println("ERROR : Cette classe ne posssedent aucune fonction");}

        for (int i = 0; i < fonction.length; i++) {
            System.out.println("                -fonction : "+fonction[i].getName());
            Annotation[] annotations = fonction[i].getAnnotations();    //maka nale annotations
            if (annotations.length == 0) {
                System.out.println("                    -> NULL EXCEPTION : Cette fonction aucune annotation"); }
            for (Annotation annotation : annotations) {
                System.out.println("                    -> annotation"+annotation);                         //print
            }
            System.out.println();
        }
}

/*==============fonction show field's annotation==============
        BUT : afficher annotation d'attribut dans une classe
        INTERET : manao sysout auto 
*/
    public void showfieldannotation(Class objclass) {       //nom du class en arguement
        Field[] attribut = objclass.getDeclaredFields();    //maka anle field rehetra
        if (attribut.length == 0) { System.out.println("ERROR : Cette classe ne posssedent aucun attribut");}     

        for (int i = 0; i < attribut.length; i++) {
            System.out.println("            -attribut : "+attribut[i].getName());
            Annotation[] annotations = attribut[i].getAnnotations();    //maka nale annotations
            if (annotations.length == 0) {
                System.out.println("                -> NULL EXCEPTION : Cet attribut aucune annotation"); }     
            for (Annotation annotation : annotations) {
                System.out.println("                -> annotation"+annotation);                         //print 
            }
            System.out.println();
        }
    }
/*==============fonction show class' annotation==============
        BUT : afficher toutes la annotations dans une classe
        INTERET : manao sysout auto 
*/
    public void showclassannotation(String packagename) throws Exception{   //nom du package en arguement
        AllClass call = new AllClass();                                     //miantso fonction maka class rehetra
        List<Class> allclasses = call.getClasses(packagename);              //on a toutes les class dans une list

        for (int i = 0; i < allclasses.size(); i++) {
            System.out.println("CLASS : "+allclasses.get(i).getSimpleName());                               //print le nom de la class
            Annotation[] mines = allclasses.get(i).getAnnotations();                                        //prend toutes les annotations de cette class
            if (mines.length == 0) { System.out.println("ERROR : Cette classe ne contient aucune annotation");}     //si la class ne contient aucune annotation -> message
            for (Annotation annotation : mines) { System.out.println("    -> annotation de la class: "+annotation); }  //print le nom de annotations 
            this.showfieldannotation(allclasses.get(i));
            this.showmethodannotation(allclasses.get(i));
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            System.out.println();
        }
    }

    // public List<String> getinfo(String urlinfos) {
    //     return List.of(urlinfos.split("/"));
    // }


}
