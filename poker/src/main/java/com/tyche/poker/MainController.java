package com.tyche.poker;

import com.tyche.poker.model.PokerTable;
import com.tyche.poker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.tyche.poker.Card.cardsInPlay;
import static com.tyche.poker.Card.compareCards;

@Controller // must be Controller (not RestController) for Thymeleaf...
public class MainController {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PokerTableRepository pokerTableRepository;


    @GetMapping(path="/")
    public String register() {
        return "register.html";
    }


    @GetMapping(path="/room")
    public String room(Model model,String uuid) {

        try{
            if (getUser(uuid) == null) throw new Exception();
            User thisUser = getUser(uuid);

            //test to see if you have no chips!
            if(thisUser.getChips() == 0 && thisUser.isMyTurn() == true) return "loser.html";

            Iterable<User> currentUsersIterable = getAllUsers();
            List<User> otherUsers = StreamSupport.stream(currentUsersIterable.spliterator(), false).collect(Collectors.toList());
            otherUsers.remove(thisUser); // filter out your user from the list of other users

            try{
                PokerTable tableState = StreamSupport.stream(getAllTables().spliterator(), false).collect(Collectors.toList()).get(0);

                model.addAttribute("thisUser", thisUser);
                model.addAttribute("otherUsers", otherUsers);
                model.addAttribute("tableState", tableState);

            } catch(Exception e){
                PokerTable tableState = new PokerTable();
                tableState.setPot(0);
                tableState.setUuid("null");
                tableState.setFlop0("null");

                model.addAttribute("thisUser", thisUser);
                model.addAttribute("otherUsers", otherUsers);
                model.addAttribute("tableState", tableState);

            }

            return "room.html";
        } catch (Exception e){
            return "register.html";
        }
    }


    @PostMapping(path = "/users")
    public RedirectView newUser(@RequestBody String name) {
        String uuid = UUID.randomUUID().toString();
        User newUser = new User();
        newUser.setUuid(uuid);
        newUser.setName(name.substring(6));
        newUser.setChips(1000);
        newUser.setCard0("null");
        newUser.setCard1("null");
        userRepository.save(newUser);

        // when a new user is added, we want to update the view of users already in the game
        // how do we refresh the page of /room?uuid= of users in the DB?
        updateState();

        RedirectView rv = new RedirectView("/room");
        rv.addStaticAttribute("uuid", uuid);
        return rv;
    }


    public User getUser(String uuid) {
        return userRepository.findByUuid(uuid);
    }


    @GetMapping(path="/start")
    public @ResponseBody String startGame(){
        deleteAllTables();
        newRoundSetUp();
        return "Let the Hunger Games begin!";
    }


    @GetMapping(path="/users")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }


    @GetMapping(path="/users/delete")
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }


    @GetMapping(path="/users/delete/{name}")
    public @ResponseBody String deleteUserByName(@PathVariable String name) {
            User userToExecute = userRepository.findByName(name);
            userRepository.delete(userToExecute);
            updateState();
            return "successfully wiped the user: " + name;
    }


    @GetMapping(path="/tables")
    public @ResponseBody Iterable<PokerTable> getAllTables() {
        return pokerTableRepository.findAll();
    }


    @GetMapping(path="/tables/delete")
    public void deleteAllTables() {
        pokerTableRepository.deleteAll();
    }


    @GetMapping(path="/flood")
    public @ResponseBody String flood(){
            deleteAllUsers();
            deleteAllTables();
            updateState();
            return "successfully wiped the game";
    }


    //Anytime that userRepository edits the DB, we need to refresh the state for each "room" perspective
    public void updateState(){
        System.out.println("state change");
        // trigger all open "room" perspectives to refresh
        // trigger refresh at the "room" level, regardless of of UUID
    }


    public void newRoundSetUp(){
        PokerTable table = new PokerTable();
        table.setPot(0);
        table.setUuid(UUID.randomUUID().toString());
        //table.setFlop0(new Card().toString()); -> no longer wanted...
        pokerTableRepository.save(table);

        Iterable<User> currentUsersIterable = getAllUsers();
        List<User> currentUsers = StreamSupport.stream(currentUsersIterable.spliterator(), false).collect(Collectors.toList());

        currentUsers.get(0).setMyTurn(true); // first player always bet first, MVP

        for(User user : currentUsers){
            user.setCard0((new Card()).toString());
            user.setCard1((new Card()).toString());
            userRepository.save(user);
            cardsInPlay.add(user.getCard0());
            cardsInPlay.add(user.getCard1());
        }

        updateState();
        // need to push these changes to the "room" views


    }


    @PostMapping(path = "/turn")
    public RedirectView makeTurn(@RequestBody String uuid_action_betValue) {

        // extract uuid, action & betValue
        // uuid=eea3d544-1b9a-4e55-8a61-663a9cf9c99d&action=rais&betValue=10
        String uuid = uuid_action_betValue.substring(5,41);
        String action = uuid_action_betValue.substring(49,53);
        int betValue = Integer.parseInt(uuid_action_betValue.substring(63));

        // update the state of this user and that of the table
        User thisUser = getUser(uuid);
        thisUser.setMyTurn(false);

        Iterable<PokerTable> thisTableIter = getAllTables();
        List<PokerTable> thisTableList = StreamSupport.stream(thisTableIter.spliterator(), false).collect(Collectors.toList());
        PokerTable thisTable = thisTableList.get(0);

        switch(action){
            case "fold":
                thisUser.setFold(true);
                break;
            case "call":
                try {
                    if((thisTable.getCurrentBet() != 0) && (thisUser.getChips() >= thisTable.getCurrentBet()) && (thisUser.getChips() - thisTable.getCurrentBet() >= 0)){
                        thisUser.setMyBet(thisTable.getCurrentBet());
                        thisUser.setChips(thisUser.getChips() - thisUser.getMyBet());
                        thisTable.setCurrentBet(thisUser.getMyBet());
                        thisTable.setPot(thisTable.getPot() + thisUser.getMyBet());
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    thisUser.setMyTurn(true);
                    RedirectView rv = new RedirectView("/turn/invalid");
                    return rv;
                }
                break;
            case "chec":
                    try {
                        if(thisTable.getCurrentBet() == 0){
                            thisUser.setMyBet(0);
                        } else {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        thisUser.setMyTurn(true);
                        RedirectView rv = new RedirectView("/turn/invalid");
                        return rv;
                    }
                break;
            case "rais":
                try { // also need to add a rule: a raise must take your current bet for this betting turn to be > the current table bet!
                    if(thisUser.getChips() - betValue >= 0 && betValue > thisTable.getCurrentBet()){
                        thisUser.setMyBet(betValue);
                        thisUser.setChips(thisUser.getChips() - thisUser.getMyBet());
                        thisTable.setCurrentBet(thisUser.getMyBet());
                        thisTable.setPot(thisTable.getPot() + thisUser.getMyBet());
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    thisUser.setMyTurn(true);
                    RedirectView rv = new RedirectView("/turn/invalid");
                    return rv;
                }
                break;
            case "alli":
                if(thisUser.getChips() > thisTable.getCurrentBet()){
                    thisUser.setMyBet(thisUser.getChips());
                    thisUser.setChips(0);
                    thisTable.setPot(thisTable.getPot() + thisUser.getMyBet());

                    thisTable.setCurrentBet(thisUser.getMyBet());
                } else {
                    thisUser.setMyBet(thisUser.getChips());
                    thisUser.setChips(0);
                    thisTable.setPot(thisTable.getPot() + thisUser.getMyBet());
                }
                break;
        }


        pokerTableRepository.save(thisTable);


        // need to move the turn along to the next user...
        boolean nextTurn = false;
        Iterable<User> allUsersIter = getAllUsers();
        List<User> allUsersList = StreamSupport.stream(allUsersIter.spliterator(), false).collect(Collectors.toList());
        int index = allUsersList.indexOf(thisUser);
        int len = allUsersList.size();
        if (index == len - 1){ // index = len - 1
            // end of turn, last user has been
            // Hold on! does anyone need to bet again? (for each user, if bet < table bet and not folded, must bet again)

            for(User user:allUsersList){
                if(user.getMyBet() < thisTable.getCurrentBet() && user.isFold() == false){
                    user.setMyTurn(true);
                    // save and return to room (however how do we skip user's who don't need to bet again)
                    userRepository.save(thisUser);
                    updateState();
                    // RETURN A REDIRECT BACK TO THE PAGE THIS REQUEST CAME FROM!
                    RedirectView rv = new RedirectView("/room");
                    rv.addStaticAttribute("uuid", uuid);
                    return rv;
                }
            }

            // HERE BOYS!
            // if all players have bet and flop == null, set flop and all bet again
            // if all players have bet and turn == null, set turn and all bet again
            // if all players have bet and river == null, set river and all bet again
            // if all players have bet and river != null, end the turn!

            if(thisTable.getFlop0() == null ){
                thisTable.setFlop0(new Card().toString());
                thisTable.setFlop1(new Card().toString());
                thisTable.setFlop2(new Card().toString());
                cardsInPlay.add(thisTable.getFlop0());
                cardsInPlay.add(thisTable.getFlop1());
                cardsInPlay.add(thisTable.getFlop2());
                thisTable.setCurrentBet(0);
                for(User user:allUsersList){
                    user.setMyBet(0);
                }

                User nextUser = allUsersList.get(0);
                nextUser.setMyTurn(true);
                userRepository.save(nextUser);
                pokerTableRepository.save(thisTable);
            } else if(thisTable.getTurn() == null){
                thisTable.setTurn(new Card().toString());
                cardsInPlay.add(thisTable.getTurn());
                thisTable.setCurrentBet(0);
                for(User user:allUsersList){
                    user.setMyBet(0);
                }

                User nextUser = allUsersList.get(0);
                nextUser.setMyTurn(true);
                userRepository.save(nextUser);
                pokerTableRepository.save(thisTable);
            } else if (thisTable.getRiver() == null){
                thisTable.setRiver(new Card().toString());
                cardsInPlay.add(thisTable.getRiver());
                thisTable.setCurrentBet(0);
                for(User user:allUsersList){
                    user.setMyBet(0);
                }

                User nextUser = allUsersList.get(0);
                nextUser.setMyTurn(true);
                userRepository.save(nextUser);
                pokerTableRepository.save(thisTable);
            } else if (thisTable.getRiver() != null){
                endTurn(thisTable);
                for(User user:allUsersList){
                    user.setMyBet(0);
                }
                User nextUser = allUsersList.get(0);
                userRepository.save(nextUser);
                nextTurn = true;
            }
        } else{
            // Re-raise: if the nextUser has has currentBet > 0 and currentBet >= table bet, skip 'em
            User nextUser = allUsersList.get(index + 1);
            nextUser.setMyTurn(true);
            if (nextUser.getMyBet() > 0 && nextUser.getMyBet() >= thisTable.getCurrentBet()){
                makeTurn("uuid="+nextUser.getUuid()+"&action=null&betValue=0"); // dud call!
            }
            userRepository.save(nextUser);
        }

        userRepository.save(thisUser);


        updateState();

        if (nextTurn){
            newRoundSetUp();
        }


        // RETURN A REDIRECT BACK TO THE PAGE THIS REQUEST CAME FROM!
        RedirectView rv = new RedirectView("/room");
        rv.addStaticAttribute("uuid", uuid);
        return rv;

    }


    @GetMapping(path="/turn/invalid")
    public String invalidTurn() {
        return "invalidTurn.html";
    }


    public void endTurn(PokerTable thisTable){
        // compare cards of non-folded users
        Iterable<User> usersIter = getAllUsers();
        List<User> usersList = StreamSupport.stream(usersIter.spliterator(), false).collect(Collectors.toList());
        Card card = new Card();

        // remove those who have folded
        for (User user : usersList){
            if (user.isFold()){
                usersList.remove(user);
            }
        }

        // dish out pot to the user with the highest card (split on draw)
        List<User> winners = Card.compareCards(usersList, thisTable);
        for(User winner : winners){
            int winnings = thisTable.getPot() / winners.size();
            winner.setChips(winner.getChips() + winnings);
        }

        // wipe table db
        pokerTableRepository.delete(thisTable);
        System.out.println("cards in play: " + cardsInPlay.toString()); // test
        cardsInPlay.clear();
    }


    @GetMapping(path="/test/evaluate")
    public @ResponseBody List<User> comparatorTest(){

        List<User> usersList = new ArrayList<>();
        usersList.add(new User("001", "4♥", "2♦"));
        usersList.add(new User("002", "2♥", "J♣"));
        List<User> winners = Card.compareCards(usersList, new PokerTable("2♠", "3♠",  "Q♥",  "Q♦", "Q♠"));

        return winners;
    }



}
