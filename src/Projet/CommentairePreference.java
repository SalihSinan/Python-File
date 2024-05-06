package Projet;

import java.io.*;
import java.util.ArrayList;

public class CommentairePreference implements Serializable {
    private static final long serialVersionUID = 1L;

    private String auteur;
    private String version;

    public CommentairePreference(String auteur, String version) {
        this.auteur = auteur;
        this.version = version;
    }

    //Obternir auteur
    public String getAuteur() {
        return auteur;
    }

    //Definir auteur
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    //Obtenir Verion
    public String getVersion() {
        return version;
    }

    //Definir Version
    public void setVersion(String version) {
        this.version = version;
    }

    //Serialisation et enregistrememnt
    public void serialize(String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
        }
    }

    //Deserialisation
    public CommentairePreference deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (CommentairePreference) ois.readObject();
        }
    }

    public void ajouterPyDocSiManquant(String fichierPython, String auteur, String version) throws IOException {
        // Créer un BufferedReader pour lire le fichier.
        try (BufferedReader lecteur = new BufferedReader(new FileReader(fichierPython))) {

            //liste pour stocker toutes les ligne.
            ArrayList<String> lignesDuFichier = new ArrayList<>();

            // Déclarer une variable pour stocker chaque ligne.
            String ligneActuelle;

            //vérifier si un PyDoc manque.
            boolean pyDocManquant = false;

            // chaque ligne du fichier jusqu'à la fin du fichier.
            while ((ligneActuelle = lecteur.readLine()) != null) {
                // Vérifier si la ligne est le début d'une définition.
                if (ligneActuelle.trim().startsWith("def")) {
                    // Extraire le nom de la fonction.
                    String nomDeLaFonction = extraireNomDeFonction(ligneActuelle);
                    // Ajouter la ligne de définition.
                    lignesDuFichier.add(ligneActuelle);
                    // Extraire les paramètres de la fonction.
                    String[] parametres = extraireParametres(ligneActuelle);
                    // Lire la ligne suivante après la définition de la fonction.
                    ligneActuelle = lecteur.readLine();

                    // Vérifier si la ligne suivante est nulle.
                    if (ligneActuelle == null || !ligneActuelle.trim().startsWith("\"\"\"")) {
                        // Si c'est le cas, le PyDoc manque.
                        pyDocManquant = true;
                        // Ajouter un début de PyDoc.
                        lignesDuFichier.add("    \"\"\"");
                        //  version, l'auteur et les paramètres.
                        ajouterAnnotationsPyDocGenerique(lignesDuFichier, parametres, nomDeLaFonction);
                    }

                    // Vérifier si la ligne n'est pas le début d'une nouvelle fonction.
                    if (ligneActuelle != null && !ligneActuelle.trim().startsWith("def")) {
                        // Si non, l'ajouter à la liste.
                        lignesDuFichier.add(ligneActuelle);
                    }
                } else {
                    lignesDuFichier.add(ligneActuelle);
                }
            }

         // Vérifier si un PyDoc manquait.
            if (pyDocManquant) {
                // Si oui, créer un BufferedWriter pour réécrire
                try (BufferedWriter redacteur = new BufferedWriter(new FileWriter(fichierPython))) {
                    // Écrire ligne dans le fichier.
                    for (String ligne : lignesDuFichier) {
                        // Remplacer les valeurs réelles
                        ligne = ligne.replace("@Nom de l'auteur", auteur).replace("@Numero version", version);
                        redacteur.write(ligne);
                        redacteur.newLine();
                    }
                }
            }
        }
    }

    private void ajouterAnnotationsPyDocGenerique(ArrayList<String> lignes, String[] parametres, String nomDeLaFonction) {
    	// Ajouter description de la fonction.
    	lignes.add("    @Brief description de : " + nomDeLaFonction);

    	//  l'auteur et les paramètres.
    	lignes.add("    @Numéro version " + getVersion());
    	lignes.add("    @Nom de l'auteur " + getAuteur());

    	// Ajouter une ligne @param .
    	for (String parametre : parametres) {
    		// Vérifier que le paramètre n'est pas une chaîne vide.
    		if (!parametre.trim().isEmpty()) {
    			// Extraire nom paramètre.
    			String parametreTrim = parametre.trim();
    			String nomParametre = (parametreTrim.contains(" ")) ? parametreTrim.split(" ")[0] : parametreTrim;
    			// Ajouter la ligne @paraM.
    			lignes.add("    @Description du param " + nomParametre);
    		}
    	}

// Ligne @return avec le nom de la fonction.
lignes.add("    @return " + nomDeLaFonction);

// Ajouter la fin du PyDoc générique.
lignes.add("    \"\"\"");
}


    //Extraire nom
    private String extraireNomDeFonction(String ligne) {
        String[] division = ligne.split("\\s+|\\(");
        return (division.length > 1) ? division[1] : "";
    }

    //Extraire param
    private String[] extraireParametres(String ligne) {
        return ligne.substring(ligne.indexOf('(') + 1, ligne.indexOf(')')).split(",");
    }
    
}


