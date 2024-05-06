package Projet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AjouterSquelettePydoc {

    public void ajouterPyDocSiManquant(String fichierPython) throws IOException {
        // Créer un BufferedReader pour lire le fichier source Python.
        try (BufferedReader lecteur = new BufferedReader(new FileReader(fichierPython))) {

            // Initialiser une liste pour stocker toutes les lignes lues du fichier.
            ArrayList<String> lignesDuFichier = new ArrayList<>();

            // Déclarer une variable pour stocker chaque ligne lue du fichier.
            String ligneActuelle;

            // Un drapeau pour vérifier si un PyDoc manque dans la fonction actuelle.
            boolean pyDocManquant = false;

            // Boucler sur chaque ligne du fichier jusqu'à la fin du fichier.
            while ((ligneActuelle = lecteur.readLine()) != null) {
                // Vérifier si la ligne actuelle est le début d'une définition de fonction.
                if (ligneActuelle.trim().startsWith("def")) {
                    // Extraire le nom de la fonction de la ligne de définition.
                    String nomDeLaFonction = extraireNomDeFonction(ligneActuelle);
                    // Ajouter la ligne de définition de la fonction à la liste des lignes.
                    lignesDuFichier.add(ligneActuelle);
                    // Extraire les paramètres de la fonction.
                    String[] parametres = extraireParametres(ligneActuelle);
                    // Lire la ligne suivante après la définition de la fonction.
                    ligneActuelle = lecteur.readLine();

                    // Vérifier si la ligne suivante est nulle ou si elle ne commence pas par un PyDoc.
                    if (ligneActuelle == null || !ligneActuelle.trim().startsWith("\"\"\"")) {
                        // Si c'est le cas, le PyDoc manque pour cette fonction.
                        pyDocManquant = true;
                        // Ajouter un début de PyDoc générique à la liste des lignes.
                        lignesDuFichier.add("    \"\"\"");
                        // Ajouter les annotations de PyDoc pour la version, l'auteur et les paramètres.
                        ajouterAnnotationsPyDocGenerique(lignesDuFichier, parametres, nomDeLaFonction);
                    }

                    // Vérifier si la ligne n'est pas le début d'une nouvelle fonction.
                    if (ligneActuelle != null && !ligneActuelle.trim().startsWith("def")) {
                        // Si non, l'ajouter à la liste des lignes.
                        lignesDuFichier.add(ligneActuelle);
                    }
                } else {
                    // Si la ligne n'est pas une définition de fonction, l'ajouter à la liste des lignes.
                    lignesDuFichier.add(ligneActuelle);
                }
            }
            
    		// Vérifier si un PyDoc manquait et qu'il a été ajouté.
            if (pyDocManquant ) {
                // Si oui, créer un BufferedWriter pour réécrire le fichier avec les modifications.
                try (BufferedWriter redacteur = new BufferedWriter(new FileWriter(fichierPython))) {
                    // Écrire chaque ligne dans le fichier.
                    for (String ligne : lignesDuFichier) {
                        redacteur.write(ligne);
                        redacteur.newLine();
                    }
                }
                
                System.out.println("Le squellette a était ajouter.");
            }else {
            System.out.println("Le squellette n'a pas était ajouter car les fonction sen possède déja.");
        }
        
        }
        
        }

        


    private String extraireNomDeFonction(String ligne) {
        String[] division = ligne.split("\\s+|\\(");
        return (division.length > 1) ? division[1] : "";
    }

    private String[] extraireParametres(String ligne) {
        return ligne.substring(ligne.indexOf('(') + 1, ligne.indexOf(')')).split(",");
    }

    private void ajouterAnnotationsPyDocGenerique(ArrayList<String> lignes, String[] parametres, String nomDeLaFonction) {
        // Ajouter les annotations de PyDoc pour la version, l'auteur et les paramètres.
        lignes.add("    @version 0.1");
        lignes.add("    @author ML");
        // Boucler sur chaque paramètre pour ajouter une ligne @param pour chacun.
        for (String parametre : parametres) {
            // Vérifier si le paramètre n'est pas une chaîne vide.
            if (!parametre.trim().isEmpty()) {
                // Nettoyer le paramètre et extraire son nom.
                String parametreTrim = parametre.trim();
                String nomParametre = (parametreTrim.contains(" ")) ? parametreTrim.split(" ")[0] : parametreTrim;
                // Ajouter la ligne @param à la liste des lignes.
                lignes.add("    @param " + nomParametre);
            }
        }
        // Ajouter une ligne @return avec le nom de la fonction.
        lignes.add("    @return " + nomDeLaFonction);
        // Ajouter la fin du PyDoc générique à la liste des lignes.
        lignes.add("    \"\"\"");
    }
}

	
	  

