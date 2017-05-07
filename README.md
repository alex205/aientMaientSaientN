# aientMaientSaientN
2000's famous IM revival
Relive the glorious days of MSN.

Authors : Guilhem Cichocki "Toon" // Alexis Girardi "alex205"

## Java Version

Be sure to install `Java 8` to run ths project: https://www.java.com/fr/download/

## Compilation and execution

Clone this repository in any directory of your computer.

### With Intellij

Start a *new JavaFX Project* with the existing source files and select the correct JDK (Java 8).
You just have to compile the whole project and run **controller.main**.

### Another way ?

  **Comment compiler puis exécuter.(Tous les intervenants ne compileront pas votre code, mais certains souhaitent le faire).**

## Features

### What can you do with this software ?

- Choose a nickname
- Connection/disconnection to the network
- Send a message/file/nudge to any other connected user
- Choose a status (available, away, busy, fake offline)
- Choose a profile picture
- Choose a personnal message that you want to share
- Choose your text color

### How can we improve this software ?

- Send an emote to any connected user
- Video/Sound streaming between users
- Add games
- Group conversation

 
## Test report 

Authors : Guilhem Cichocki "Toon" // Alexis Girardi "alex205"

### Model (JUnit Tests)

#### ContactTest.java

To be sure that users are unic, we created the `fullpseudo` (*username*@*ip_adress*).

**public void getFullPseudo()** => This test allow us to get the fullPseudo of a `Contact`. **Success**

#### ContactCollectionTest

**public void getInstance()** => Test that we can get the instance of the contact collection. It is a singleton. **Success**

**public void addContact()** => Test if we can add a contact in the collection. **Success**

**public void delContact()**  => Test if we can remove a contact from the collection. **Success**

**public void contactExists()** => Test if we can know if a contact exists in the collection. **Success**

**public void getContact()** => Test if we can get a contact from the collection. **Success**

### Network

Because of our singleton patterns in the *package Network*, we encountered some issues with JUnit tests.
We chose to develop a **Parrot** which allows us to test our network. The **parrot** simply send back everything it receives as it was his action.
This system showed that our messages, files, status, personnal messages, profile pictures are sent through the network to the **parrot** and come back.

This is the list of those tests :
**message sent to the parrot** => The parrot sent it back. **Success**
**file sent to the parrot** => The parrot sent it back. **Success**
**change your text color** => The parrot change his text color and notify all the other users. **Success**
**change your status** => The parrot change his own status and notify all the other users. **Success**
**change the personnal message** => The parrot change his own and notify all the other users. **Success**
**change your profile picture** => The aprrot change his own but failed to notify the other users. **Fail**

 
 
### Test with other groups
 
 o Et enfin, un rapport de tests inter-projets : certains tests (pas nécessairement tous) appliqués par vous-même au projet d'un ou deux autres binômes. (Ceux qui travaillent en Java 7 ne pourront pas tester les projets écrits en Java .

   Pour que les tests soient jouables sur les projets des autres binômes, vous aurez besoin de définir collectivement une interface commune et une ou plusieurs classes d'adaptation entre l'interface et votre projet (par exemple, l'envoi de message ne s'appelle pas forcément sendMessage chez tout le monde, et les arguments peuvent varier).

 Notez que l'interface commune ne contient qu'un petit nombre de méthodes, et peut être découpée en plusieurs Interfaces java.

