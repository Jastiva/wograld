object spell_party_heal
inherit type_spell
name party heal
name_pl party heal
skill praying
msg
This prayer heals all wounds on all party members.

However, it will not cure other problems, such as blindness or disease.
endmsg
other_arch spell_heal
sound wn/heal
face spell_praying.x11
hp 0
grace 75
dam 9999
level 35
subtype 48
path_attuned 256
value 3
invisible 1
no_drop 1
casting_time 24
end

