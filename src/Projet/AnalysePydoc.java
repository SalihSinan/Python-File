package Projet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalysePydoc {
	
	//Methode d'analyse
    public List<String> analyserFonctionsAvecPydoc(String cheminFichier) {
    	//Creation d'une liste pour le stockage des foncions avec commentaire pydoc
        List<String> fonctionsAvecPydoc = new ArrayList<>();
        //Creation d'une liste pour le reste
        List<String> fonctionsSansPydoc = new ArrayList<>();
        //Pour trouver le nom de la fonction
        String nomFonctionCourante = null;
        //Pour savoir si nous somme a l'interieur d'un commentaire
        boolean dansDocstring = false;

        try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            
            //Lire toute les lignes du fichiers
            while ((ligne = lecteur.readLine()) != null) {
                ligne = ligne.trim();
                
                
                //Voir le debut des fonctions
                if (ligne.startsWith("def ") && ligne.endsWith(":")) {
                	//Trouver le nom de la fonctions
                    nomFonctionCourante = extraireNomFonction(ligne);
                }
                
                //Voir le debut des commentaires
                if (ligne.startsWith("\"\"\"") && !dansDocstring) {
                    dansDocstring = true;
                } else if (dansDocstring && ligne.endsWith("\"\"\"")) {
                	//Voir la fin des commentaires
                    dansDocstring = false;

                    if (nomFonctionCourante != null) {
                        fonctionsAvecPydoc.add(nomFonctionCourante);
                    }
                }
            }
        } catch (IOException e) {
        	//Voir les exceptions a cause de la lecture
            e.printStackTrace();
        }
        //La liste des fonctions sans commentaires
        fonctionsSansPydoc.addAll(listeFonctions(cheminFichier));
        //Renvoyer la liste des fonctions avec commentaires
        return fonctionsAvecPydoc;
    }
    
    
    //Methode trouver nom fonction
    private String extraireNomFonction(String ligne) {
        int debutNom = ligne.indexOf("def ") + 4;
        int finNom = ligne.indexOf("(");

        if (debutNom >= 0 && finNom >= 0) {
        	//Prendre et renvoyer nom
            return ligne.substring(debutNom, finNom).trim();
        } else {
        	//Si ceci n'est pas valide rien envoyer
            return null;
        }
    }
    
    
    //Methode liste tout les fonctions
    private List<String> listeFonctions(String cheminFichier) {
    	//Creation d'une liste pour stocker les nom
        List<String> fonctions = new ArrayList<>();

        try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            
            
            //Parcours de tout le fichier (même technique de tout a l'heure)
            while ((ligne = lecteur.readLine()) != null) {
                ligne = ligne.trim();
                
                
                //Conditions de debut de fonctions
                if (ligne.startsWith("def ") && ligne.endsWith(":")) {
                	//Trouve le nom de la fonction et l'ajoute
                    fonctions.add(extraireNomFonction(ligne));
                }
            }
        } catch (IOException e) {
        	//Voir les exceptions a cause de la lecture
            e.printStackTrace();
        }
        
        //Renvoie la liste
        return fonctions;
    }
}
