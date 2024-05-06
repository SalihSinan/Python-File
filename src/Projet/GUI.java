package Projet;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.io.File;


public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Declaration des boutons
    private JButton btnListerFichiers = new JButton("Lister Fichiers Python");
    private JButton btnAnalyserAnnotationsType = new JButton("Analyser Annotations Type");
    private JButton btnAjouterCommentaires = new JButton("Ajouter Commentaires");
    private JButton btnGenererStatistiques = new JButton("Générer Les Statistiques");
    private JButton btnAjouterShebangEtUTF8 = new JButton("Ajouter le Shebang et UTF-8");
    private JButton btnAnalyserDeuxPremieresLignes = new JButton("Analyser Deux Premières Lignes");
    private JButton btnAnalyserCommentaires = new JButton("Analyser les commentaires Pydoc");
    private JButton btnEditerFichier = new JButton("Éditer Fichier");
    private JButton btnAide = new JButton("Afficher les options possibles");
    private JButton btnPreferences = new JButton("Préférences de Commentaire");
    private JButton btnQuitter = new JButton("Quitter le programme");
    
    private JButton btnChoisirFichier = new JButton("Choisir un fichier");
    private JButton btnChoisirRepertoire = new JButton("Choisir un répertoire");

    //Zone ou sont affiche les resultats
    private JTextArea resultTextArea = new JTextArea();
    private String cheminFichierChoisi;
    private String cheminRepertoireChoisi;

    //Constructeur GUI
    public GUI(String title) {
        super(title);
        //Initialisation interface
        init();
    }

    //Methode pour initialisation
    private void init() {
        setLayout(new BorderLayout());

        // Division de l'écran en deux : à gauche les boutons, à droite le résultat
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createButtonPanel(), createResultPanel());
        add(splitPane, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }
    
    
    //Creation panneau de boutons
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(9, 1));
        buttonPanel.add(btnListerFichiers);
        buttonPanel.add(btnAnalyserAnnotationsType);
        buttonPanel.add(btnAjouterCommentaires);
        buttonPanel.add(btnGenererStatistiques);
        buttonPanel.add(btnAjouterShebangEtUTF8);
        buttonPanel.add(btnAnalyserDeuxPremieresLignes);
        buttonPanel.add(btnAnalyserCommentaires);
        buttonPanel.add(btnEditerFichier);
        buttonPanel.add(btnPreferences);
        buttonPanel.add(btnChoisirFichier);
        buttonPanel.add(btnChoisirRepertoire);
        buttonPanel.add(btnAide);
        btnQuitter.setForeground(Color.RED);//Changement de la couleur pour quitter 
        buttonPanel.add(btnQuitter);

        addActionListeners();//Ajouts des action d'evenements des boutons
        return buttonPanel;
    }

    //Ajouts des action d'evenements des boutons
    private void addActionListeners() {
        btnListerFichiers.addActionListener(new ListerFichiersAction());
        btnAnalyserAnnotationsType.addActionListener(new AnalyserAnnotationsTypeAction());
        btnAjouterCommentaires.addActionListener(new AjouterCommentairesAction());
        btnAjouterShebangEtUTF8.addActionListener(new AjouterShebangEtUTF8Action());
        btnAnalyserDeuxPremieresLignes.addActionListener(new AnalyserDeuxPremieresLignesAction());
        btnAnalyserCommentaires.addActionListener(new AnalyserCommentairesAction());
        btnEditerFichier.addActionListener(new EditerFichierAction());
        btnAide.addActionListener(new AideAction());
        btnChoisirFichier.addActionListener(new ChoisirFichierAction());
        btnChoisirRepertoire.addActionListener(new ChoisirRepertoireAction());
        btnPreferences.addActionListener(new PreferencesAction());
        btnQuitter.addActionListener(new QuitterAction());
        btnGenererStatistiques.addActionListener(new GenererStatistiquesAction());
    }

    //Creation du panneau pour afficher les resultats
    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultTextArea.setEditable(false);
        resultPanel.add(new JScrollPane(resultTextArea), BorderLayout.CENTER);
        return resultPanel;
    }
    
 // Methode pour l'affichage des texte
    private void afficherResultat(String resultat) {
        resultTextArea.setText(resultat);
    }


    private class ListerFichiersAction implements ActionListener {
        // Méthode appelée lorsque le bouton est cliqué
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cheminRepertoireChoisi != null) {
                // Utiliser le répertoire déjà choisi
                FichiersPython fichiersPython = new FichiersPython();
                List<String> fichiers = fichiersPython.chercherFichiersPython(cheminRepertoireChoisi);

                StringBuilder resultBuilder = new StringBuilder();
                for (String line : fichiers) {
                    resultBuilder.append(line).append("\n");
                }

                // Afficher resultat
                afficherResultat(resultBuilder.toString());
            } else {
                // Afficher un message d erreur quand y a pas de repertoire
                afficherResultat("Veuillez choisir un répertoire d'abord.");
            }
        }
    }


    private class AnalyserAnnotationsTypeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cheminFichierChoisi != null) {
                // fichier choisi
                Stat statistique = new Stat();
                List<String> fonctionsAvecAnnotations = statistique.fonctionsAvecAnnotationsType(new File(cheminFichierChoisi));

                // message par rapport au resultat
                if (!fonctionsAvecAnnotations.isEmpty()) {
                    StringBuilder resultMessage = new StringBuilder("Fonctions avec Annotations de Type :\n");
                    for (String fonction : fonctionsAvecAnnotations) {
                        resultMessage.append("- ").append(fonction).append("\n");
                    }
                    afficherResultat(resultMessage.toString());
                } else {
                    afficherResultat("Aucune annotation de type trouvée.");
                }
            } else {
                // message erreur
                afficherResultat("Veuillez choisir un fichier d'abord.");
            }
        }
    }


    private class AjouterCommentairesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cheminFichierChoisi != null) {
                // Utiliser l'objet commentairePreference existant
                CommentairePreference commentaires = commentairePreference;

                try {
                    // Appeler la méthode ajouterPyDocSiManquant 
                    commentaires.ajouterPyDocSiManquant(cheminFichierChoisi, commentaires.getAuteur(), commentaires.getVersion());
                    afficherResultat("Le fichiers a été mis à jour avec succès.");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    afficherResultat("Erreur lors de l'ajout des commentaires : " + e1.getMessage());
                }
            } else {
                // Afficher un message d'erreur 
                afficherResultat("Veuillez choisir un fichier d'abord.");
            }
        }
    }

    
    private class AjouterShebangEtUTF8Action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cheminFichierChoisi != null) {
                // fichier choisi
                Ligne ligne = new Ligne();
                try {
                    // Lire fichier
                    List<String> lignes = Files.readAllLines(Paths.get(cheminFichierChoisi), StandardCharsets.UTF_8);
                    // Vérification 
                    if (!ligne.contientShebangEtUTF8(lignes)) {
                        // Ajout sinon
                        ligne.ajouterDeuxPremieresLignes(cheminFichierChoisi);
                        afficherResultat("Le Shebang et l'UTF-8 ont été ajoutés avec succès.");
                    } else {
                        afficherResultat("Le programme possède déjà le Shebang et l'UTF-8.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    afficherResultat("Erreur lors de la lecture du fichier : " + ex.getMessage());
                }
            } else {
                // message d'erreur
                afficherResultat("Veuillez choisir un fichier d'abord.");
            }
        }
    }

    private class AnalyserDeuxPremieresLignesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cheminFichierChoisi != null) {
                // fichier choisi
                Ligne ligne = new Ligne();
                try {
                    // Lire fichier
                    List<String> lignes = Files.readAllLines(Paths.get(cheminFichierChoisi), StandardCharsets.UTF_8);
                    // Vérification s'il le contient
                    boolean contientShebangUTF8 = ligne.contientShebangEtUTF8(lignes);

                    // Affichage résultats
                    if (contientShebangUTF8) {
                        afficherResultat("Oui, ce fichier contient le Shebang et l'UTF-8.");
                    } else {
                        afficherResultat("Non, ce fichier ne contient pas le Shebang et l'UTF-8.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    afficherResultat("Erreur lors de la lecture du fichier : " + ex.getMessage());
                }
            } else {
                // message d'erreur 
                afficherResultat("Veuillez choisir un fichier d'abord.");
            }
        }
    }

    
    private class AnalyserCommentairesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cheminFichierChoisi != null) {
                // fichier choisi
                AnalysePydoc analysePydoc = new AnalysePydoc();

                // Création d'une instance avec analyse pydoc
                List<String> fonctionsAvecPydoc = analysePydoc.analyserFonctionsAvecPydoc(cheminFichierChoisi);
                StringBuilder resultatText = new StringBuilder("Fonctions avec Pydoc :\n");
                for (String fonction : fonctionsAvecPydoc) {
                    resultatText.append("- ").append(fonction).append("\n");
                }

                // Afficher le résultat
                afficherResultat(resultatText.toString());
            } else {
                //message d'erreur 
                afficherResultat("Veuillez choisir un fichier d'abord.");
            }
        }
    }


    private class AideAction implements ActionListener {
        @Override
        //Afficher message avec option
        public void actionPerformed(ActionEvent e) {
            String optionsMessage = "Options possibles:\n" +
            		" - Lister les Fichiers\n"+
                    " - Analyser Les Annotations Type d'un fichier\n" +
                    " - Verifier Les Deux Premieres Lignes d'un Fichier\n" +
                    " - Analyser les commentaire Pydoc\n" +
                    " - Ajouter le Shebang et UFT-8\n" +
                    " - Ajouter Les Commentaire Pydoc\n" +
                    " - Editer le Fichier\n"+
                    " - Initialiser les preference de commentaires\n"+
                    " - Generer les Statisques d'un Fichier\n"+
                    " - Quitter le programme";

            afficherResultat(optionsMessage);
        }
    }
    
    private class GenererStatistiquesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cheminRepertoireChoisi != null) {
                // répertoire choisi
                Stat statistique = new Stat();
                String resultatStatistiques = statistique.genererStat(cheminRepertoireChoisi);

                // Affichage résultat
                afficherResultat(resultatStatistiques);
            } else {
                //message d'erreur 
                afficherResultat("Veuillez choisir un répertoire d'abord.");
            }
        }
    }


    private class EditerFichierAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cheminFichierChoisi != null) {
                try {
                    // Lire toutes les lignes
                    List<String> lignes = Files.readAllLines(Paths.get(cheminFichierChoisi), StandardCharsets.UTF_8);
                    StringBuilder contenuFichier = new StringBuilder();
                    for (String ligne : lignes) {
                        contenuFichier.append(ligne).append("\n");
                    }

                    // Création d'une zone pour éditer
                    JTextArea textArea = new JTextArea(contenuFichier.toString());
                    textArea.setRows(20);
                    textArea.setColumns(80);

                    // Ajout d'une barre de défilement
                    JScrollPane scrollPane = new JScrollPane(textArea);

                    // Affichage d'une boîte pour les modifications
                    int result = JOptionPane.showConfirmDialog(GUI.this, scrollPane, "Éditer le fichier",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    // Enregistrement des modifications si l'utilisateur le souhaite
                    if (result == JOptionPane.OK_OPTION) {
                        String nouveauContenu = textArea.getText();
                        Files.write(Paths.get(cheminFichierChoisi), nouveauContenu.getBytes());
                        afficherResultat("Le fichier a été édité avec succès.");
                    } else {
                        afficherResultat("L'édition du fichier a été annulée.");
                    }
                } catch (IOException ex) {
                    afficherResultat("Erreur lors de la lecture/écriture du fichier : " + ex.getMessage());
                }
            } else {
                // Afficher un message d'erreur ou une indication à l'utilisateur
                afficherResultat("Veuillez choisir un fichier d'abord.");
            }
        }
    }

    
    //Creation d'instance avec CommentairePreference
    private CommentairePreference commentairePreference = new CommentairePreference("", "");

    private class PreferencesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Demande le nom et la version
            String auteur = JOptionPane.showInputDialog(GUI.this, "Nom de l'auteur :", commentairePreference.getAuteur());
            String version = JOptionPane.showInputDialog(GUI.this, "Numéro de version :", commentairePreference.getVersion());

            // Mise à jour des infos
            commentairePreference.setAuteur(auteur);
            commentairePreference.setVersion(version);

            // Appeler sauvegarderPreference
            sauvegarderPreferences();
        }
    }

    
 // Mettez à jour la méthode sauvegarderPreferences pour utiliser l'objet commentairePreference existant
    private void sauvegarderPreferences() {
        try {
            // Appelle sérialisation de commentairePreference
            commentairePreference.serialize("preferences.ser");
            afficherResultat("Préférences sauvegardées avec succès.");
        } catch (IOException ex) {
            afficherResultat("Erreur lors de la sauvegarde des préférences : " + ex.getMessage());
        }
    }


    private class QuitterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	//fermeture de la page
            System.exit(0);
        }
    }
    
    private class ChoisirFichierAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choisir un fichier");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int choice = chooser.showOpenDialog(GUI.this);
            if (choice == JFileChooser.APPROVE_OPTION) {
                cheminFichierChoisi = chooser.getSelectedFile().getAbsolutePath();
                afficherResultat("Fichier choisi : " + cheminFichierChoisi);
            }
        }
    }

    private class ChoisirRepertoireAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choisir un répertoire");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int choice = chooser.showOpenDialog(GUI.this);
            if (choice == JFileChooser.APPROVE_OPTION) {
                cheminRepertoireChoisi = chooser.getSelectedFile().getAbsolutePath();
                afficherResultat("Répertoire choisi : " + cheminRepertoireChoisi);
            }
        }
    }

    //Methode principale pour le GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI("Analyseur Fichier Python"));
    }
}





