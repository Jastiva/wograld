object spell_party_restoration
inherit type_spell
name party restoration
name_pl party restoration
skill praying
msg
Heals all damage, confusion, poison, and disease, and also provides a full
stomach of food to all party members. In other words, it restores about
any negative effect you might suffer from.

The only thing it does not take care of is actual death.
endmsg
other_arch spell_restoration
sound wn/heal
face spell_praying.x11
grace 120
food 999
dam 9999
level 45
subtype 48
attacktype 37749792
path_attuned 256
value 3
invisible 1
no_drop 1
casting_time 30
end

