# Projet Socialnetwork

Matthieu Jan - Info2

Félix Bezançon - Info2

# RMI
## Présentation générale
L'application de java RMI au projet Socialnetwork se compose de :
- Une implementation de ProfileManager avec :
    - Une interface commune
    - Une implementation sur le serveur sous forme de RemoteServer
    - Une implementation sur le client qui va faire des appels au serveur (via l'interface commune)
- Une implementation de RMICallback avec :
    - Une interface client, associé à une implémentation client
    - Une interface serveur, associé à une implémentation serveur
- Un main pour le client qui lance le GUI
- UN main pour le serveur qui crée les deux RemoteServer utilisé

## Termes
Une rapide liste des termes utilisés
- Une classe XXXImpl est une implémentation de l'interface XXX
- Une classe RMIXXX est une classe réalisé pour la partie 1 (qui nous concerne ici)

## Main(s)
Présentation des lanceurs des applications
### ServerMain
Initialise le Registre de RMI, en lui associant un objet ProfileManagerRemoteImpl et un objet RMICallbackServerImpl

### ClientMain
Initialise le Socialnetwork object et la frame

## ProfileManager
### RMIProfileManagerRemote (common)
L'interface reprend les méthodes du packages logic, permettant ainsi d'avoir une communication qui ressemble au fonctionnement et soit facile à utilisé.

### RMIProfileManager(client)
Implémente directement le ProfileManager du package logic. Ces méthodes adaptent quelques éléments (notement les callback) mais font essentiellement des appels aux méthodes de l'objet RMIProfileManagerRemote instancié sur le serveur

### RMIProfileManagerRemoteImpl(server)
Implémente l'interface de common, et est utilisé par le client qui l'appelle pour réalisé ses actions (signup, login ...).

Il stocke plusieurs Hashmap gérer :
- les enregistrements
- les connexions
- les mots de passes.

Il fait ensuite certaines opérations pour assurer le bon comportement du système (vérifié l'unicité, la validité des tokens ...). Il utilise l'objet callback pour communiquer les modifications de profile aux utilisateurs.

## RMICallback
RMICallback fonctionne en deux parties et en trois temps :
Deux parties :
- Un RMICallbackClient, côté client
- Un RMICallbackServer, côté serveur

Trois temps :
- Le serveur met a disposition un objet RMICallbackServerImpl qui va permettre d'enregistrer des RMICallbackClient
- Le client enregistre un objet RMICallbackClientImpl auprès du serveur
- Lorsqu'un update est fait, le serveur invoque les objets enregistrés pour faire les mise à jour.
