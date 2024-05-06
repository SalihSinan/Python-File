package Projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Stat {
	
	
	public List<String> fonctionsAvecAnnotationsType(File fichier) {
        List<String> fonctionsAvecAnnotations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            Pattern pattern = Pattern.compile("\\s*def\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(");

            while ((ligne = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(ligne);

                if (matcher.find() && contientAnnotationType(ligne)) {
                    String nomFonction = matcher.group(1);
                    fonctionsAvecAnnotations.add(nomFonction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fonctionsAvecAnnotations;
    }
	
	

    // ANALYSEZ UN REPERTOIRE -STAT
	public String genererStat(String cheminRepertoire) {
        File repertoire = new File(cheminRepertoire);

        if (repertoire.isDirectory()) {
            System.out.println("Statistique du Répertoire : '" + repertoire.getName() + "'");
            int totalFonctions = 0;
            int totalAnnotations = 0;
            int nbFichiers = 0;
            int nbUTF = 0;
            int nbPydoc = 0; // Nouvelle variable pour les commentaires Pydoc
            double pourcentage = 0;
            double pourcentageUTF = 0;

            // Utiliser la méthode chercher pour récupérer la liste des fichiers Python
            List<String> fichiersPython = new ArrayList<>();
            chercher(cheminRepertoire, fichiersPython);

            for (String fichier : fichiersPython) {
                System.out.println("Fichier Python trouvé : " + fichier);

                int fonctionsDansFichier = compterFonctionsDansFichier(new File(fichier));
                int fonctionsAnnotations = compterFonctionsAnnotations(new File(fichier));
                int totalutf = comptershebang(new File(fichier));

                nbFichiers++;
                totalFonctions += fonctionsDansFichier;
                totalAnnotations += fonctionsAnnotations;
                nbUTF += totalutf;

                // Comptage des commentaires Pydoc
                nbPydoc += compterCommentairesPydoc(new File(fichier));
            }

            // Calcul des statistiques comme avant
            pourcentage = (totalFonctions > 0) ? ((double) totalAnnotations / totalFonctions) * 100 : 0;
            pourcentageUTF = (nbFichiers > 0) ? ((double) nbUTF / nbFichiers) * 100 : 0;
            

            double pourcentagePydoc = 0;

            if (nbPydoc > 0) {
                pourcentagePydoc = ((double) nbPydoc / totalFonctions) * 100;
            }

            String resultat = "-Nombres de fichiers analysés  : " + nbFichiers + "\n" +
                    "-Nombre total de fonctions  : " + totalFonctions + "\n" +
                    "-Nombre total de fonctions Annotés : " + totalAnnotations + "\n" +
                    "-Pourcentage de fonctions Annotées dans tous les fichiers : " + pourcentage + "%\n" +
                    "-Pourcentage total des fichiers avec shebang et utf-8 : " + pourcentageUTF + "%\n" +
                    "-Pourcentage total des commentaires Pydoc : " + pourcentagePydoc + "%";

            return resultat;
        }

        return "Le chemin n'est pas un répertoire";
    }


    private void chercher(String chemin, List<String> fichiersPython) {
        File repertoire = new File(chemin);

        if (repertoire.isDirectory()) {
            File[] fichiers = repertoire.listFiles();

            if (fichiers != null) {
                for (File fichier : fichiers) {
                    if (fichier.isFile() && fichier.getName().endsWith(".py")) {
                        fichiersPython.add(fichier.getAbsolutePath());
                    } else if (fichier.isDirectory()) {
                        System.out.println("Exploration du sous-répertoire : " + fichier.getAbsolutePath());
                        chercher(fichier.getAbsolutePath(), fichiersPython);
                    }
                }
            }
        }
    }

    private int compterCommentairesPydoc(File fichier) {
        int nombreLignesPydoc = 0;
        int nombreLignesAvecTexte = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            boolean enCommentairePydoc = false;

            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();

                if (ligne.startsWith("\"\"\"") && !enCommentairePydoc) {
                    enCommentairePydoc = true;
                    if (ligne.length() > 3) {
                        // La ligne commence par """ et contient du texte
                        nombreLignesAvecTexte++;
                    }
                } else if (ligne.endsWith("\"\"\"") && enCommentairePydoc) {
                    enCommentairePydoc = false;
                    if (ligne.length() > 3) {
                        // La ligne se termine par """ et contient du texte
                        nombreLignesAvecTexte++;
                    }
                    nombreLignesPydoc++;
                } else if (enCommentairePydoc) {
                    // La ligne est à l'intérieur d'un commentaire Pydoc
                    nombreLignesAvecTexte++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Vérifier si au moins une ligne de commentaire Pydoc contient du texte
        return (nombreLignesPydoc > 0 && nombreLignesAvecTexte > 0) ? nombreLignesPydoc : 0;
    }
 

    private int compterFonctionsDansFichier(File fichier) {
        int nombreFonctions = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            Pattern pattern = Pattern.compile("\\s*def\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(");

            while ((ligne = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(ligne);
                if (matcher.find()) {
                    nombreFonctions++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nombreFonctions;
    }

    public int compterFonctionsAnnotations(File fichier) {
        int nombreAnnotations = 0;
        Set<String> fonctionsDejaTraitees = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            Pattern pattern = Pattern.compile("\\s*def\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(");

            while ((ligne = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(ligne);

                if (matcher.find()) {
                    String nomFonction = matcher.group(1);

                    if (contientAnnotationType(ligne) && !fonctionsDejaTraitees.contains(nomFonction)) {
                        nombreAnnotations++;
                        fonctionsDejaTraitees.add(nomFonction);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nombreAnnotations;
    }
    
    private  int comptershebang(File fichier) {
    	
    	int nb=0;
    	String ligne;
        boolean shebangTrouve = false;
    	try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            while ((ligne = br.readLine()) != null) {
               if (contientUTF(ligne)) {
                   
            	   if (!shebangTrouve) {
                       nb++;
                       shebangTrouve = true; // Marquer le shebang comme trouvé
                   }
               }
            }
    	}catch (IOException e) {
            e.printStackTrace();
        }
        
	
    	return nb;	
    }

    public boolean contientAnnotationType(String ligne) {
        return ligne.contains(":") && ligne.contains("->");
    }
    
    private  boolean contientUTF(String ligne) {
        // Vérifier si la ligne contient une annotation de type dans une docstring Python
        return ligne.contains("#!/usr/bin/env python") || ligne.contains("# coding: utf-8");
    


    	
    	
    }
}