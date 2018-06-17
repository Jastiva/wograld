object spell_party_protection_from_attack
inherit type_spell
name party protection from attack
name_pl party protection from attack
skill praying
msg
Creates a powerful
force that protects your party members from
physical damage.

While the effect stacks with armour the characters are wearing, it will be
weaker in that case.
endmsg
other_arch spell_protection_from_attack
face spell_praying.x11
grace 75
maxgrace 40
level 24
subtype 48
resist_physical 40
path_attuned 1
value 3
invisible 1
duration 500
duration_modifier 4
dam_modifier 3
no_drop 1
casting_time 30
end

