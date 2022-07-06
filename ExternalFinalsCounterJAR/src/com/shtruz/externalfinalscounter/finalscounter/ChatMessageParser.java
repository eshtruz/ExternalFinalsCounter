package com.shtruz.externalfinalscounter.finalscounter;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.shtruz.externalfinalscounter.ExternalFinalsCounter;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.shtruz.externalfinalscounter.mapping.Mappings.*;

public class ChatMessageParser {
    private final ExternalFinalsCounter externalFinalsCounter;
    private static final String[] KILL_MESSAGES = { " was shot and killed by ", " was snowballed to death by ", " was killed by ", " was killed with a potion by ", " was killed with an explosion by ", " was killed with magic by ", " was filled full of lead by ", " was iced by ", " met their end by ", " lost a drinking contest with ", " was killed with dynamite by ", " lost the draw to ", " was struck down by ", " was turned to dust by ", " was turned to ash by ", " was melted by ", " was incinerated by ", " was vaporized by ", " was struck with Cupid's arrow by ", " was given the cold shoulder by ", " was hugged too hard by ", " drank a love potion from ", " was hit by a love bomb from ", " was no match for ", " was smote from afar by ", " was justly ended by ", " was purified by ", " was killed with holy water by ", " was dealt vengeful justice by ", " was returned to dust by ", " be shot and killed by ", " be snowballed to death by ", " be sent to Davy Jones' locker by ", " be killed with rum by ", " be killed with rum by ", " be shot with cannon by ", " be killed with magic by ", " was glazed in BBQ sauce by ", " was sprinkled with chilli powder by ", " was sliced up by ", " was overcooked by ", " was deep fried by ", " was boiled by ", " was injected with malware by ", " was DDoS'd by ", " was deleted by ", " was purged by an antivirus owned by ", " was fragmented by ", " was corrupted by ", " was squeaked from a distance by ", " was hit by frozen cheese from ", " was chewed up by ", " was chemically cheesed by ", " was turned into cheese whiz by ", " was magically squeaked by " };
    private final Map<String, Integer> allPlayers = new HashMap<>();
    private final Map<String, Integer> blue = new HashMap<>();
    private final Map<String, Integer> green = new HashMap<>();
    private final Map<String, Integer> red = new HashMap<>();
    private final Map<String, Integer> yellow = new HashMap<>();
    private final List<String> deadPlayers = new ArrayList<>();
    private String bluePrefix = "\u00A79";
    private String greenPrefix = "\u00A7a";
    private String redPrefix = "\u00A7c";
    private String yellowPrefix = "\u00A7e";
    private boolean blueWitherDead = false;
    private boolean greenWitherDead = false;
    private boolean redWitherDead = false;
    private boolean yellowWitherDead = false;

    public ChatMessageParser(ExternalFinalsCounter externalFinalsCounter) {
        this.externalFinalsCounter = externalFinalsCounter;
    }

    public Map<String, Integer> getAllPlayers() {
        return allPlayers;
    }

    public Map<String, Integer> getBlue() {
        return blue;
    }

    public Map<String, Integer> getGreen() {
        return green;
    }

    public Map<String, Integer> getRed() {
        return red;
    }

    public Map<String, Integer> getYellow() {
        return yellow;
    }

    public String getBluePrefix() {
        return bluePrefix;
    }

    public String getGreenPrefix() {
        return greenPrefix;
    }

    public String getRedPrefix() {
        return redPrefix;
    }

    public String getYellowPrefix() {
        return yellowPrefix;
    }

    public String getFinalsInTabString(String playerName) {
        if (externalFinalsCounter.getConfig().finalsInTab) {
            if (allPlayers.containsKey(playerName)) {
                return " " + "\u00A7e" + allPlayers.get(playerName);
            }
        }

        return "";
    }

    public void reset() {
        allPlayers.clear();
        blue.clear();
        green.clear();
        red.clear();
        yellow.clear();
        deadPlayers.clear();
        blueWitherDead = false;
        greenWitherDead = false;
        redWitherDead = false;
        yellowWitherDead = false;

        externalFinalsCounter.getFinalsCounterRenderer().update();
    }

    private List<String> getScoreboardLines() {
        List<String> lines = new ArrayList<>();

        try {
            Object minecraft = getMinecraftMethod.invoke(null);

            Object theWorld = theWorldField.get(minecraft);

            if (theWorld == null) {
                return lines;
            }

            Object scoreboard = getScoreboardMethod.invoke(theWorld);

            if (scoreboard == null) {
                return lines;
            }

            Object objective = getObjectiveInDisplaySlotMethod.invoke(scoreboard, 1);

            if (objective == null) {
                return lines;
            }

            Collection<Object> scores = (Collection<Object>) getSortedScoresMethod.invoke(scoreboard, objective);
            List<Object> list = scores
                    .stream()
                    .filter(input -> {
                        try {
                            if (input != null) {
                                String playerName = (String) sGetPlayerNameMethod.invoke(input);

                                return playerName != null && !playerName.startsWith("#");
                            }
                        } catch (IllegalAccessException | InvocationTargetException exception) {
                            exception.printStackTrace();
                        }

                        return false;
                    }).collect(Collectors.toList());

            if (list.size() > 15) {
                scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
            } else {
                scores = list;
            }

            for (Object score : scores) {
                String playerName = (String) sGetPlayerNameMethod.invoke(score);
                Object team = getPlayersTeamMethod.invoke(scoreboard, playerName);

                lines.add((String) formatPlayerNameMethod.invoke(null, team, playerName));
            }

            return lines;
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }

        return lines;
    }

    public void onChat(Object iChatComponent) {
        try {
            Object minecraft = getMinecraftMethod.invoke(null);

            Object currentServerData = getCurrentServerDataMethod.invoke(minecraft);

            if (currentServerData == null) {
                return;
            }

            String serverIP = (String) serverIPField.get(currentServerData);
            if (!serverIP.toLowerCase().endsWith("hypixel.net")) {
                return;
            }

            Object theWorld = theWorldField.get(minecraft);

            if (theWorld == null) {
                return;
            }

            Object scoreboard = getScoreboardMethod.invoke(theWorld);

            if (scoreboard == null) {
                return;
            }

            Object objective = getObjectiveInDisplaySlotMethod.invoke(scoreboard, 1);

            if (objective == null) {
                return;
            }

            String scoreboardTitle = (String) getDisplayNameMethod.invoke(objective);
            scoreboardTitle = (String) stripControlCodesMethod.invoke(null, scoreboardTitle);
            if (!scoreboardTitle.contains("MEGA WALLS")) {
                return;
            }

            String unformattedText = (String) getUnformattedTextMethod.invoke(iChatComponent);

            if (unformattedText.equals("                                 Mega Walls")) {
                reset();
                return;
            }

            for (String killMessage : KILL_MESSAGES) {
                Matcher matcher = Pattern.compile("(\\w+)"
                                + killMessage
                                + "(\\w+)")
                        .matcher(unformattedText);

                if (matcher.lookingAt()) {
                    for (String line : getScoreboardLines()) {
                        if (line.contains("[B]")) {
                            bluePrefix = line.substring(0, 2);
                            blueWitherDead = !line.contains("Wither");
                        } else if (line.contains("[G]")) {
                            greenPrefix = line.substring(0, 2);
                            greenWitherDead = !line.contains("Wither");
                        } else if (line.contains("[R]")) {
                            redPrefix = line.substring(0, 2);
                            redWitherDead = !line.contains("Wither");
                        } else if (line.contains("[Y]")) {
                            yellowPrefix = line.substring(0, 2);
                            yellowWitherDead = !line.contains("Wither");
                        }
                    }

                    String killed = matcher.group(1);
                    String killer = matcher.group(2);

                    String formattedText = (String) getFormattedTextMethod.invoke(iChatComponent);

                    String killedPrefix = formattedText.substring(formattedText.indexOf(killed) - 2, formattedText.indexOf(killed));
                    String killerPrefix = formattedText.substring(formattedText.indexOf(killer) - 2, formattedText.indexOf(killer));

                    if (killedPrefix.equals(bluePrefix) && blueWitherDead) {
                        blue.remove(killed);
                    } else if (killedPrefix.equals(greenPrefix) && greenWitherDead) {
                        green.remove(killed);
                    } else if (killedPrefix.equals(redPrefix) && redWitherDead) {
                        red.remove(killed);
                    } else if (killedPrefix.equals(yellowPrefix) && yellowWitherDead) {
                        yellow.remove(killed);
                    } else {
                        return;
                    }

                    allPlayers.remove(killed);
                    deadPlayers.add(killed);

                    if (!killed.equals(killer) && !deadPlayers.contains(killer)) {
                        if (killerPrefix.equals(bluePrefix)) {
                            blue.put(killer, blue.getOrDefault(killer, 0) + 1);
                        } else if (killerPrefix.equals(greenPrefix)) {
                            green.put(killer, green.getOrDefault(killer, 0) + 1);
                        } else if (killerPrefix.equals(redPrefix)) {
                            red.put(killer, red.getOrDefault(killer, 0) + 1);
                        } else if (killerPrefix.equals(yellowPrefix)) {
                            yellow.put(killer, yellow.getOrDefault(killer, 0) + 1);
                        }

                        allPlayers.put(killer, allPlayers.getOrDefault(killer, 0) + 1);
                    }

                    externalFinalsCounter.getFinalsCounterRenderer().update();

                    return;
                }
            }
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }
}
