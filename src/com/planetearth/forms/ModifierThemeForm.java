package com.planetearth.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.planetearth.beans.Theme;
import com.planetearth.dao.DAOException;
import com.planetearth.dao.ThemeDao;
import com.planetearth.dao.TypeDao;

public class ModifierThemeForm {
    private static final String CHAMP_NOM         = "nomTheme";
    private static final String CHAMP_DESCRIPTION = "description";
    private static final String CHAMP_ID_TYPE     = "listeTypes";
    private static final String CHAMP_ID          = "idTheme";

    private ThemeDao            themeDao;
    private TypeDao             typeDao;

    public ModifierThemeForm( ThemeDao themeDao, TypeDao typeDao ) {
        this.themeDao = themeDao;
        this.typeDao = typeDao;
    }

    private String              resultat;
    private Map<String, String> erreurs = new HashMap<String, String>();

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Theme modifierTheme( HttpServletRequest request ) {
        String nom = getValeurChamp( request, CHAMP_NOM );
        String description = getValeurChamp( request, CHAMP_DESCRIPTION );
        String idTheme = getValeurChamp( request, CHAMP_ID );
        String idType = getValeurChamp( request, CHAMP_ID_TYPE );

        Theme theme = new Theme();

        try {
            traiterNom( nom, theme );
            traiterDescription( description, theme );
            traiterId( Long.parseLong( idTheme ), theme );

            theme.setType( typeDao.read( Long.parseLong( idType ) ) );

            if ( erreurs.isEmpty() ) {
                themeDao.update( theme );
                resultat = "Thème modifié avec succès.";
            } else {
                resultat = "Échec de l'operation.";
            }
        } catch ( DAOException e ) {
            setErreur( "imprévu ", "Erreur imprévue lors de la modification." );
            resultat = "Échec de la modification du thème : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        } catch ( NumberFormatException e ) {
            setErreur( "imprévu ", "Erreur imprévue lors de la modification." );
            resultat = "Échec de l'operation.";
        }

        return theme;
    }

    private void traiterId( Long id, Theme theme ) {
        try {
            validationId( id );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_ID, e.getMessage() );
        }
        theme.setId( id );
    }

    private void traiterDescription( String description, Theme theme ) {
        try {
            validationDescription( description );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_DESCRIPTION, e.getMessage() );
        }
        theme.setDescription( description );
    }

    private void traiterNom( String nom, Theme theme ) {
        try {
            validationNom( nom );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_NOM, e.getMessage() );
        }
        theme.setNom( nom );
    }

    private void validationId( Long id ) throws FormValidationException {
        if ( id == null || id <= 0 ) {
            throw new FormValidationException( "ID invalide." );
        }
    }

    private void validationNom( String nom ) throws FormValidationException {
        if ( nom != null && nom.length() < 5 ) {
            throw new FormValidationException( "Le nom du théme doit contenir au moins 5 caractères." );
        } else if ( nom == null ) {
            throw new FormValidationException( "Merci de saisir le nom d'un théme." );
        }
    }

    private void validationDescription( String description ) throws FormValidationException {
        if ( description != null && description.length() < 10 ) {
            throw new FormValidationException( "La description doit contenir au moins 10 caractères." );
        } else if ( description == null ) {
            throw new FormValidationException( "Merci de saisir la description du théme." );
        }
    }

    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}
