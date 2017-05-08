# aientMaientSaientN
2000's famous IM revival.

Relive the glorious days of MSN.

Authors : Guilhem Cichocki "Toon" // Alexis Girardi "alex205"

## Java Version

Be sure to install `Java 8` to run this project: https://www.java.com/fr/download/

## Compilation and execution

### Build from source
Clone this repository in any directory of your computer.


Start a **new JavaFX Project** with the existing source files in your favorite IDE and select the correct JDK (Java 8).
You just have to compile the whole project and run `controller.main`.

### Binary release

You can download a binary release here : http://etud.insa-toulouse.fr/~girardi/aientMaientSaientN/aientMaientSaientN.jar
Then execute `java -jar aientMaientSaientN.jar`

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
- Improve file transfert (ask confirmation for example)

 
## Test report 

*Authors : Guilhem Cichocki "Toon" // Alexis Girardi "alex205"*

### Model (JUnit Tests)

#### ContactTest.java

To be sure that users are unic, we created the `fullpseudo` (*username*@*ip_adress*).

- **public void getFullPseudo()** => This test allow us to get the fullPseudo of a `Contact`. **Success**

#### ContactCollectionTest.java

- **public void getInstance()** => Test that we can get the instance of the `ContactCollection`. It is a singleton. **Success**

- **public void addContact()** => Test if we can add a `Contact` in the `ContactCollection`. **Success**

- **public void delContact()**  => Test if we can remove a `Contact` from the `ContactCollection`. **Success**

- **public void contactExists()** => Test if we can know if a `Contact` exists in the `ContactCollection`. **Success**

- **public void getContact()** => Test if we can get a `Contact` from the `ContactCollection`. **Success**

### Network

Because of our singleton patterns in the *package Network*, we encountered some issues with JUnit tests.

We chose to develop a **Parrot** which allows us to test our network. The **parrot** simply send back everything it receives as it was his action.

This system showed that our messages, files, status, personnal messages, profile pictures are sent through the network to the **parrot** and come back.

This is the list of those tests :

- **message sent to the parrot** => The parrot sent it back. **Success**

- **file sent to the parrot** => The parrot sent it back. **Success**

- **change your text color** => The parrot change his text color and notify all the other users. **Success**

- **change your status** => The parrot change his own status and notify all the other users. **Success**

- **change the personnal message** => The parrot change his own and notify all the other users. **Success**

- **change your profile picture** => The aprrot change his own but failed to notify the other users. **Fail**

The tests aren't designed to be kept in production that's why they are not included in the jar. If you want to test the parrot, build from source and uncomment lines in the controller.
 
### Test with other groups

This software has been tested with the other group.

- **Send and receive messages** => **Success**
- **Change status** => **Success**
- **Send nudge** => Other implementations ignore this notification **Success**
