package com.planetearth.dao;

import static com.planetearth.dao.DAOUtility.close;
import static com.planetearth.dao.DAOUtility.initPreparedQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.planetearth.beans.Voyage;

public class VoyageDaoImp implements VoyageDao {
    private static final String SQL_SELECT    = "SELECT idVoyage, destination, idTheme, titre, hebergement, date, prix, duree, difficulte, altitude, description FROM Voyage ORDER BY idVoyage ASC";
    private static final String SQL_SELECT_ID = "SELECT idVoyage, destination, idTheme, titre, hebergement, date, prix, duree, difficulte, altitude, description FROM Voyage WHERE idVoyage = ?";
    private static final String SQL_INSERT    = "INSERT INTO Voyage ( destination, idTheme, titre, hebergement, date, prix, duree, difficulte, altitude, description) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE    = "DELETE FROM Voyage WHERE idVoyage = ?";
    private static final String SQL_UPDATE    = "UPDATE Voyage SET destination = ?, idTheme = ?, titre = ?, hebergement = ?, date = ?, prix = ?, duree = ?, difficulte = ?, altitude = ?, description = ? WHERE idVoyage = ?";

    private DAOFactory          daoFactory;

    public VoyageDaoImp( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Long create( Voyage voyage ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet autoGeneratedValue = null;

        try {
            connection = daoFactory.getConnection();

            preparedStatement = initPreparedQuery( connection, SQL_INSERT, true, voyage.getDestination().getNom(),
                    voyage.getTheme().getId(), voyage.getTitre(), voyage.getHebergement(),
                    new Timestamp( voyage.getDate().getMillis() ),
                    voyage.getPrix(), voyage.getDuree(), voyage.getDifficulte(), voyage.getAltitude(),
                    voyage.getDescription() );
            int status = preparedStatement.executeUpdate();
            if ( status == 0 ) {
                throw new DAOException( "Échec de la création du voyage, aucune ligne ajoutée dans la table." );
            }

            autoGeneratedValue = preparedStatement.getGeneratedKeys();
            if ( autoGeneratedValue.next() ) {
                voyage.setId( autoGeneratedValue.getLong( 1 ) );
            } else {
                throw new DAOException( "Échec de la création du voyage en base, aucun ID auto-généré retourné." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } catch ( Exception e ) {
            throw new DAOException( e );
        } finally {
            close( preparedStatement, connection );
        }

        return voyage.getId();
    }

    @Override
    public Voyage read( Long id, DestinationDao destinationDao, ThemeDao themeDao, TypeDao typeDao )
            throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Voyage voyage = null;

        try {
            connection = daoFactory.getConnection();

            preparedStatement = initPreparedQuery( connection, SQL_SELECT_ID, false, id );

            rs = preparedStatement.executeQuery();
            if ( rs.next() ) {
                voyage = map( rs, destinationDao, themeDao, typeDao );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            close( rs, preparedStatement, connection );
        }

        return voyage;
    }

    @Override
    public List<Voyage> list( DestinationDao destinationDao, ThemeDao themeDao, TypeDao typeDao ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Voyage> voyages = new ArrayList<Voyage>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initPreparedQuery( connection, SQL_SELECT, false );

            rs = preparedStatement.executeQuery();
            while ( rs.next() ) {
                voyages.add( map( rs, destinationDao, themeDao, typeDao ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            close( rs, preparedStatement, connection );
        }

        return voyages;
    }

    @Override
    public void delete( Long id ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initPreparedQuery( connection, SQL_DELETE, false, id );

            int status = preparedStatement.executeUpdate();
            if ( status == 0 ) {
                throw new DAOException( "Échec de la suppression du voyage, aucune ligne supprimée de la table." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            close( preparedStatement, connection );
        }
    }

    @Override
    public void update( Voyage voyage ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();

            preparedStatement = initPreparedQuery( connection, SQL_UPDATE, false, voyage.getDestination().getNom(),
                    voyage.getTheme().getId(), voyage.getTitre(), voyage.getHebergement(),
                    new Timestamp( voyage.getDate().getMillis() ),
                    voyage.getPrix(), voyage.getDuree(), voyage.getDifficulte(), voyage.getAltitude(),
                    voyage.getDescription(), voyage.getId() );

            int status = preparedStatement.executeUpdate();
            if ( status == 0 ) {
                throw new DAOException( "Échec de la mise a jour du voyage, aucune ligne modifier." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } catch ( Exception e ) {
            throw new DAOException( e );
        } finally {
            close( preparedStatement, connection );
        }
    }

    private static Voyage map( ResultSet rs, DestinationDao destinationDao, ThemeDao themeDao, TypeDao typeDao )
            throws SQLException {
        Voyage voyage = new Voyage();

        voyage.setId( rs.getLong( "idVoyage" ) );
        voyage.setDestination( destinationDao.read( rs.getString( "destination" ) ) );
        voyage.setTheme( themeDao.read( rs.getLong( "idTheme" ), typeDao ) );
        voyage.setTitre( rs.getString( "titre" ) );
        voyage.setHebergement( rs.getString( "hebergement" ) );
        voyage.setDate( new DateTime( rs.getTimestamp( "date" ) ) );
        voyage.setPrix( rs.getDouble( "prix" ) );
        voyage.setDuree( rs.getInt( "duree" ) );
        voyage.setDifficulte( rs.getInt( "difficulte" ) );
        voyage.setAltitude( rs.getInt( "altitude" ) );
        voyage.setDescription( rs.getString( "description" ) );

        return voyage;
    }
}
