# Projet Socialnetwork

Matthieu Jan - Info2

Félix Bezançon - Info2


# OpenJMS
## Présentation générale
L'application d'OpenJMS au projet Socialnetwork pour gérer les demandes d'amis et l'envoi / publication de message se compose de :

- La réalisation d'un OpenJmsTalker se chargeant du maintient des sessions et de la communication.
- Les implémentations de FriendManager et MessageManager
- La réalisation de deux lanceurs d'évenements, FriendEventCaller et MessageEventCaller pour fire des events
- Une classe JmsType dans le pacckage common pour faciliter l'écriture des messages.

De plus, quelques adaptations on été réalisé auprès du projet pour intégrer OpensJms :

- Les méthodes logins et logout de RmiProfileManager (côté client) appellent les méhtodes liés d'OpenJmsTalker
- La méthode register de RMIProfileManagerRemoteImpl (côté server) créé les Destinations associés à l'utilisateur.
- La classe ServerMain démarre le serveur openjms

## Fonctionnement
- Lorsqu'un utilisateur s'enregistre auprès du serveur, celui ci lui créé une compte sur le serveur openjms, une queue privée (format email+priv) et un topic public (format email+pub) qui seront les destinations qu'il utilise pour communiquer avec ses petits camarades.
- Lorsqu'un utilisateur se connecte, OpenJmsTalker créé une session qu'il va conserver jusqu'à la déconnexion.
- Lorsqu'un utilisateur se connecte, il se met à écouter sur sa propre queue privée.
- Lorsqu un utilisateur souhaite communiquer avec un autre, il dépose un message sur la queue privée du destinataire
    - Le destinataire récupere le message, puis le traite.
- Lorque deux utilisateurs sont amis, ceux ci écoutent les files publiques de l'autre.
    - Lorsque l'un publie un message, il le dépose sur sa file publiques, écouté par ses amis.
- Lorsqu'un utilisateur se déconnecte, OpenJmsTalker ferme la session.

<div style="page-break-after: always;"></div>

# RMI
## Présentation générale
L'application de java RMI au projet Socialnetwork se compose de :
- Une implémentation de ProfileManager avec :
    - Une interface commune
    - Une implémentation sur le serveur sous forme de RemoteServer
    - Une implémentation sur le client qui va faire des appels au serveur (via l'interface commune)
- Une implémentation de RMICallback avec :
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
L'interface reprend les méthodes du packages logic, permettant ainsi d'avoir une communication qui ressemble au fonctionnement et soit facile à utiliser.

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

