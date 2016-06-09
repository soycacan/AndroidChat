package com.android.pascual.androidchat;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by pascual on 6/8/2016.
 */
public class FirebaseHelper {
    private Firebase dataReference;

    private final static String SEPARATOR = "___";
    private final static String CHATS_PATH = "chats";
    private final static String USERS_PATH = "users";
    private final static String CONTACTS_PATH = "contacts";
    private final static String FIREBASE_URL = "https://android-chat-example.firebaseio.com";

    //Clase que solo inicica la instancia
    private static class SingletonHolder {
        private static final FirebaseHelper INSTANCE = new FirebaseHelper();
    }

    //Singleton, solo la usa
    public static FirebaseHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //Constructor pasando ubicacion
    public FirebaseHelper() {
        this.dataReference = new Firebase(FIREBASE_URL);
    }

    //getter a datareference
    public Firebase getDataReference() {
        return dataReference;
    }

    //Regoer el correo del autenticado
    public String getAuthUserEmail() {
        AuthData authData = dataReference.getAuth();
        //deffinir email vacio
        String email = null;
        //valida si aun se llamo o no
        if (authData != null) {
            //buscar que es un Map
            Map<String, Object> providerdata = authData.getProviderData();
            email = providerdata.get("email").toString();
        }
        return email;
    }

    //ubicar referencia a usuario logeado
    public Firebase getUserReference(String email) {
        Firebase userReference = null;
        if (userReference != null) {
            //firebase no acepta puntos, se cambia para leer y escribir
            String emailKey = email.replace(".", "_");
            //obtener referencia de usuario
            userReference = dataReference.getRoot().child(USERS_PATH).child(emailKey);
        }
        return userReference;
    }

    //Obtener mi propia referencia
    public Firebase getMyUserReference() {
     return getUserReference(getAuthUserEmail());
    }

    //obtener los contactos
    public Firebase getContactsReference(String email) {
        return getUserReference(email).child(CONTACTS_PATH);
    }

    //obtener mis contactos
    public Firebase getMyContactsReference() {
        return getContactsReference(getAuthUserEmail());
    }

    //No entendi pa quemierda es este metodo
    public Firebase getOneContactReference(String mainEmail, String childEmail) {
        String childKey = childEmail.replace(".", "_");
        return getUserReference(mainEmail).child(CONTACTS_PATH).child(childKey);
    }

    //xxx
    public Firebase getChatsReference(String receiver) {
        String keySender = getAuthUserEmail().replace(".", "_");
        String keyReceiver = receiver.replace(".", "_");

        //Ubicar referencia de chats entre usuarios
        String keychat = keySender + SEPARATOR + keyReceiver;

        //compara e invierte para ordenar alfabeticamente
        if (keySender.compareTo(keyReceiver) > 0) {
            keychat = keyReceiver + SEPARATOR + keySender;
        }
        return dataReference.getRoot().child(CHATS_PATH).child(keychat);
    }

    public void changeUserConectionStatus(boolean online) {
        if (getMyUserReference() != null) {
            //construir un nuevo Map (key-value)
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("online", online);
            //actualiza el valor
            getMyUserReference().updateChildren(updates);
            //avisar a los demas que mi estatus cambio
            notifyContactsOfConnectionChage(online);
        }
    }

    //notificar cambio de status
    private void notifyContactsOfConnectionChage(boolean online) {
        notifyContactsOfConnectionChage(online, false);
    }

    //cerrar sesion (Offline)
    private void signoff() {
        notifyContactsOfConnectionChage(false, true);
    }

    //Metodo pricipal para notificar a los contactos
    private void notifyContactsOfConnectionChage(final boolean online, final boolean signoff) {
        final String myEmail = getAuthUserEmail();
        //agregar listener para un solo evento
        getMyContactsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //tira un snapshot
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String email = child.getKey();
                    //cerrar inversamente my status en sus perfiles
                    Firebase reference = getOneContactReference(email,myEmail);
                    reference.setValue(online);
                }
                if (signoff){
                    //desconectar la referencia
                    dataReference.unauth();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
