package com.rodrigo.tickets.database;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.rodrigo.tickets.config.InitialUserConfig;

@Service
public class DatabaseStatusService {
    
    private final DataSource dataSource;
    private volatile DatabaseStatus status = DatabaseStatus.UNKNOWN;
    private final InitialUserConfig initialUserConfig;
    
    public DatabaseStatusService(DataSource dataSource, InitialUserConfig initialUserConfig){
        this.dataSource = dataSource;
        this.initialUserConfig = initialUserConfig;
    }

    @Scheduled(fixedDelay = 5000)
    public void checkConnection(){

        DatabaseStatus previousStatus = status;
        DatabaseStatus newStatus = testConnection();

        status = newStatus;
        if(previousStatus != DatabaseStatus.CONNECTED && newStatus == DatabaseStatus.CONNECTED)
        {
            onDatabaseConnected();
        }

        if(previousStatus != newStatus){
            System.out.println("Databse status: " + newStatus);
        }
    }

    private void onDatabaseConnected(){
        try{
            initialUserConfig.initializeAdmin();
        }catch(Exception exception){
            System.err.println("Database Connected, "
                + "but initial admin "
                + "could not be created"
            );
            exception.printStackTrace();
        }
    }

    private DatabaseStatus testConnection(){
        try(Connection connection = dataSource.getConnection()){
                if(connection.isValid(1)){
                return DatabaseStatus.CONNECTED;
            }
        }catch(Exception exception){

        }
        return  DatabaseStatus.DISCONNECTED;
    }

    public DatabaseStatus getStatus(){
        return status;
    }

    public boolean isConnected(){
        return status == DatabaseStatus.CONNECTED;
    }
}
