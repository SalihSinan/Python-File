package Projet;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CLI {

	//Constructeur CLI
    public CLI() {
        super();
    }

    //Methode message help
    public void help() {
        System.out.println("Plusieurs options s'offrent à vous:\n"
        		+"\n"
                + "1) '-d' : Pour parcourir tous les fichiers Python d'un dossier et de ses sous-dossiers.\n"
                +"\n"
        		+ "- '--stat' : Pour afficher les statistiques de qualité d'un ensemble de fichiers.\n"
        		+"\n"
                + "2) '-f' : Pour analyser un fichier Python spécifique suivi de ses comandes :.\n"
                +"\n"
                + "- '--type' : Pour vérifier les annotations de type.\n"
                + "- '--head' : Pour vérifier les deux premières lignes de commentaire.\n"
                + "- '--pydoc' : Pour vérifier les commentaires de fonction au format 'pydoc'.\n"
                + "- '--sbutf8' : Pour ajouter les 2 premières lignes de commentaire si elles sont manquantes.\n"
                + "- '--comment' : Pour ajouter un squelette de commentaire 'pydoc' sur les fonctions sans commentaires.\n"
                +"\n"
                + "Relancez la console avec l'une de ces commandes suivi du CHEMIN du dossier / fichier.\n");
    }

    public static void main(String[] args) throws IOException {
    	//Creation d'instance
        CLI cli = new CLI();

        if (args.length == 0) {
            System.out.println("Veuillez relancer le programme en saisissant la commande -h ou --help pour obtenir de l'aide");
        } else {
                
        	//Parcours des arguments
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                //Cas ou l'option est -h ou --help
                    case "-h":
                    case "--help":
                    	//Affichage de help
                        cli.help();
                        break;
                    //Cas ou l'option est -d
                    case "-d":
                    	//Verif si y a un autre arguments
                        if (i + 1 < args.length) {
                            //Option --stat
                            if (args.length >= 3 && args[2].equals("--stat")) {
                                String cheminRepertoire = args[i + 1];
                                Stat stat = new Stat();
                                //Affichage des stats du repertoire
                                System.out.println(stat.genererStat(cheminRepertoire));
                            } else {
                                String cheminRepertoire = args[i + 1];
                                FichiersPython repertoire = new FichiersPython();
                                //Recherche des ficiers pythons 
                                List<String> Chemin = repertoire.chercherFichiersPython(cheminRepertoire);

                                //Afficher fichier trouve
                                repertoire.afficherFichiersPython(Chemin);
                            }
                        } else {
                            System.out.println("L'option -d nécessite un argument supplémentaire spécifiant le dossier.");
                        }
                        break;
                    //Cas ou l'option est -f
                    case "-f":
                    	//Verification si y a d'autre chose
                        if (i + 1 < args.length) {
                            String filePath = args[i + 1];
                            File fichierPython = new File(filePath);

                            //Verification de l'option supp
                            if (args.length >= 3) {
                                for (int j = 2; j < args.length; j++) {
                                    String option = args[j];

                                    //Les differentes option possible
                                    switch (option) {
                                    //Cas --type
                                        case "--type":
                                            Stat stat = new Stat();
                                            //Affichage fonction annote
                                            System.out.println("Le fichier contient " + stat.compterFonctionsAnnotations(fichierPython) + " fonction d'annotation de type");
                                            break;
                                            //Cas --head
                                        case "--head":
                                            Ligne ligne = new Ligne();
                                            //Aficchagee de la verif des deux premiere ligne
                                            System.out.println(ligne.contientdeuxpremiereligne(filePath));
                                            break;
                                            //Cas --pydoc
                                        case "--pydoc":
                                            AnalysePydoc pydoc = new AnalysePydoc();
                                            //Affiche com pydoc
                                            System.out.println("Voici les commentaires en format pydoc : " + pydoc.analyserFonctionsAvecPydoc(filePath) + "\n");
                                            break;
                                            //Cas --comment
                                        case "--comment":
                                            AjouterSquelettePydoc sq = new AjouterSquelettePydoc();
                                            //Ajout et affichage de la reponse
                                            
                                            sq.ajouterPyDocSiManquant(filePath);
                                            
                                           
                                            
                                            break;
                                            //Cas --sbutf8
                                        case "--sbutf8":
                                            Ligne ligneSbUtf8 = new Ligne();
                                            //Ajout et affichage de la rep
                                            boolean ajoutSbUtf8 = ligneSbUtf8.ajouterDeuxPremieresLignes(filePath);
                                            
                                            if (ajoutSbUtf8) {
                                                System.out.println("Les deux premières lignes ont été ajoutées avec succès.");
                                            } 
                                            
                                            break;
                                    }
                                }
                            } else {
                                System.out.println("Aucune option spécifiée.Veuillez en spécifiée une");
                            }
                        } else {
                            System.out.println("L'option -f nécessite un argument supplémentaire spécifiant le fichier.");
                        }
                        break;

                }
            }
        }
    }
    
    
}



