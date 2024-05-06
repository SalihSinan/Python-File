package Projet;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class FichiersPython {

	//Methode pour prendre les info du fichier
    private String getFileInfo(File fichier, String nomRepertoire) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        //Taille fichier
        long fileSize = fichier.length();
        //Conerversion en ko 
        String sizeInKB = String.format("%.2f", fileSize / 1024.0);

        //Dernier date de modifications du fichier
        String formattedDate = dateFormat.format(new Date(fichier.lastModified()));

        //Renvoyer cette phrase 
        return "Nom du repertoire : " + nomRepertoire + " - Nom du fichier : " + fichier.getName() + " - Taille: " + sizeInKB + " Ko, Modifié le: " + formattedDate;
    }

    //Methode recursive pour la recherche dans un repertoire
    private void chercher(String chemin, List<String> fichiersPython, String folderName) {
        File repertoire = new File(chemin);

        //Verification que c'est un repertoire
        if (repertoire.isDirectory()) {
            File[] fichiers = repertoire.listFiles();

            if (fichiers != null) {//Verifier qu'il y a des des choses a l'interieur du repertoire
            	//Parcours de tout les fichier un par un
                for (File fichier : fichiers) {
                	//Ajout des fichier pythons dans la liste
                    if (fichier.isFile() && fichier.getName().endsWith(".py")) {
                        fichiersPython.add(getFileInfo(fichier, folderName));
                    } 
                    //Pour les sous repertoire
                    else if (fichier.isDirectory()) {
                        chercher(fichier.getAbsolutePath(), fichiersPython, fichier.getName());
                    }
                }
            }
        }
    }

    //Methode pour la recherche
    public List<String> chercherFichiersPython(String cheminRepertoire) {
        // Création de la liste pour stockage
        List<String> fichiersPython = new ArrayList<>();
        // Création de l'objet file
        File rootFolder = new File(cheminRepertoire);
        // Appelle de la méthode recursive
        chercher(rootFolder.getAbsolutePath(), fichiersPython, rootFolder.getName());
        // Renvoyer la liste avec les fichiers
        return fichiersPython;
    }

    //Methode pour afficher
    public void afficherFichiersPython(List<String> fichiersPython) {
        System.out.println("\n\n");
        System.out.println("\n ~Voici les Fichiers Python trouvés dans votre dossier:\n");
        System.out.println("\n\n");

        // Affichage des infos du fichier
        if (fichiersPython.isEmpty()) {
            System.out.println("\nIl n'y a aucun fichier Python dans votre répertoire.\n");
        } else {
            for (String fileInfo : fichiersPython) {
                System.out.println(fileInfo);
            }
        }
    }

    // Méthode pour obtenir les infos du fichier sous forme de liste
    public List<String> getInfosFichiersPython(String cheminRepertoire) {
        List<String> fichiersPython = chercherFichiersPython(cheminRepertoire);
        return fichiersPython;
    }
}
