object spell_party_protection_from_paralysis
inherit type_spell
name party protection from paralysis
name_pl party protection from paralysis
skill praying
msg
This prayer gives party members increased protection from paralysis attacks.
But it won't get you a perfect protection, be careful, you can still
be paralysed!
endmsg
other_arch spell_protection_from_paralysis
face spell_praying.x11
grace 30
maxgrace 40
level 23
subtype 48
resist_paralyze 50
path_attuned 1
value 3
invisible 1
duration 500
duration_modifier 4
dam_modifier 4
no_drop 1
casting_time 20
end

