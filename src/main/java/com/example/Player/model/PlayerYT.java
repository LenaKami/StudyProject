package com.example.Player.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerYT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String linkyt;
    private String category;

    @ElementCollection
    @CollectionTable(name = "players_likes", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "liked_itemS")
    private List<String> like;

    @ElementCollection
    @CollectionTable(name = "players_unlikes", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "unliked_item")
    private List<String> unlike;


    private int countlike;
    private int countunlike;
}
