package Projet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Ligne {

	//Methode ajouter
    public boolean ajouterDeuxPremieresLignes(String cheminFichier) {
        try {
        	//Prendre le chemin
            Path path = Path.of(cheminFichier);
            //Lire les ligne du fichiers
            List<String> lignes = Files.readAllLines(path, StandardCharsets.UTF_8);

            //Verification si le fichier possede les deux lignes
            if (!contientShebangEtUTF8(lignes)) {
            	//Ajoute les deux premiere lignes si ce n'est pas le cas
                lignes.add(0, "#!/usr/bin/env python");
                lignes.add(1, "# coding: utf-8");

                //Les mets dans le fichiers
                Files.write(path, lignes, StandardCharsets.UTF_8);

                System.out.println("Shebang et UTF-8 ajoutés avec succès.");
                return true;
            } else {
                System.out.println("Le fichier contient déjà le Shebang ou UTF-8.");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Methode verification deux premiere lignes
    public boolean contientShebangEtUTF8(List<String> lignes) {
    	//Verification si les deux premiere liges sont equivalent a ceci
        return lignes.size() >= 2 &&
                lignes.get(0).trim().equals("#!/usr/bin/env python") &&
                lignes.get(1).trim().equals("# coding: utf-8");
    }
    
    public String contientdeuxpremiereligne(String fichier) throws IOException {
    	
    	//Chemin Fichier
    	Path path = Path.of(fichier);
    	//Lecture ligne par ligne
        List<String> lignes = Files.readAllLines(path, StandardCharsets.UTF_8);
        //Variable pour stoc le resultat
    	String res= "";
    	//Verif si le fichier contient les deux ligne
    	if (contientShebangEtUTF8(lignes)) {
    		//S'il contient affiche ceci
    		 res = "le fichier contient les deux lignes ";
    		
    		
    	}else {
    		//Sinon affiche ceci
    		res ="le fichier ne contient pas les deux lignes ";
    	}
    	//Renvoye res
    	return res;
    	
    	
    }


}
