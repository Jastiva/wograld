=head1 NAME

NPC_Dialogue

=head1 DESCRIPTION

NPC dialogue support module.

=cut

package NPC_Dialogue;

use strict;

sub has_dialogue($) {
   my ($ob) = @_;

   $ob->get_message =~ /^\@match /;
}

sub parse_message($) {
   map [split /\n/, $_, 2],
      grep length,
         split /^\@match /m,
            $_[0]
}

sub new {
   my ($class, %arg) = @_;

   my $self = bless {
      %arg,
   }, $class;

   $self->{match} ||= [parse_message $self->{npc}->get_message];

   $self;
}

sub greet {
   my ($self) = @_;

   $self->tell ("hi")
}

=item ($reply, @topics) = $dialog->tell ($msg)

Tells the dialog object something and returns its response and optionally
a number of topics that are refered to by this topic.

It supports a number of command constructs. They have to follow the
C<@match> directive, and there can be multiple commands that will be
executed in order.

=over 4

=item @parse regex

Parses the message using a perl regular expression (by default
case-insensitive). Any matches will be available as C<< $match->[$index]
>>.

If the regular expression does not match, the topic is skipped.

Example:

   @match deposit
   @parse deposit (\d+) (\S+)
   @eval bank::deposit $match->[0], $match->[1]

=item @cond perl

Evaluates the given perl code. If it returns false (or causes an
exception), the topic will be skipped, otherwise topic interpretation is
resumed.

The following local variables are defined within the expression:

=over 4

=item $who - The cf::object::player object that initiated the dialogue.

=item $npc - The NPC (or magic_ear etc.) object that is being talked to.

=item $msg - The actual message as passed to this method.

=item $match - An arrayref with previous results from C<@parse>.

=item $state - A hashref that stores state variables associated
with the NPC and the player, that is, it's values relate to the the
specific player-NPC interaction and other players will see a different
state. Useful to react to players in a stateful way. See C<@setstate> and
C<@ifstate>.

=item $flag - A hashref that stores flags associated with the player and
can be seen by all NPCs (so better name your flags uniquely). This is
useful for storing e.g. quest information. See C<@setflag> and C<@ifflag>.

=back

The environment is that standard "map scripting environment", which is
limited in the type of constructs allowed (no loops, for example).

=item @eval perl

Like C<@cond>, but proceed regardless of the outcome.

=item @msg perl

Like C<@cond>, but the return value will be stringified and prepended to
the message.

=item @setstate state value

Sets the named state C<state> to the given C<value>. State values are
associated with a specific player-NPC pair, so each NPC has its own state
with respect to a particular player, which makes them useful to store
information about previous questions and possibly answers. State values
get reset whenever the NPC gets reset.

See C<@ifstate> for an example.

=item @ifstate state value

Requires that the named C<state> has the given C<value>, otherwise this
topic is skipped.  For more complex comparisons, see C<@cond> with
C<$state>. Example:

  @match quest
  @setstate question quest
  Do you really want to help find the magic amulet of Beeblebrox?
  @match yes
  @ifstate question quest
  Then fetch it, stupid!

=item @setflag flag value

Sets the named flag C<flag> to the given C<value>. Flag values are
associated with a specific player and can be seen by all NPCs. with
respect to a particular player, which makes them suitable to store quest
markers and other information (e.g. reputation/alignment). Flags are
persistent over the lifetime of a player, so be careful :)

See C<@ifflag> for an example.

=item @ifflag flag value

Requires that the named C<flag> has the given C<value>, otherwise this
topic is skipped.  For more complex comparisons, see C<@cond> with
C<$flag>. Example:

  @match I want to do the quest!
  @setflag kings_quest 1
  Then seek out Bumblebee in Navar, he will tell you...
  @match I did the quest
  @ifflag kings_quest 1
  Really, which quets?

And Bumblebee might have:

  @match hi
  @ifflag kings_quest
  Hi, I was told you want to do the kings quest?

=item @trigger connected-id

Trigger all objects with the given connected-id. The trigger is stateful
and retains state per connected-id.

=item @addtopic topic

Adds the given topic names (separated by C<|>) to the list of topics
returned.

=back

=cut

sub tell {
   my ($self, $msg) = @_;

   my $lcmsg = lc $msg;

   topic:
   for my $match (@{ $self->{match} }) {
      for (split /\|/, $match->[0]) {
         if ($_ eq "*" || $lcmsg eq lc) {
            my $reply = $match->[1];
            my @kw;

            my @replies;
            my @match; # @match/@parse command results

            my $state = $self->{npc}{$self->{ob}->name}{dialog_state} ||= {};
            my $flag  = $self->{ob}{dialog_flag}                      ||= {};

            my %vars = (
               who   => $self->{ob},
               npc   => $self->{npc},
               state => $state,
               flag  => $flag,
               msg   => $msg,
               match => \@match,
            );

            local $self->{ob}{record_replies} = \@replies;

            # now execute @-commands (which can result in a no-match)
            while ($reply =~ s/^\@(\w+)\s*([^\n]*)\n?//) {
               my ($cmd, $args) = ($1, $2);

               if ($cmd eq "parse" || $cmd eq "match") { # match is future rename
                  no re 'eval'; # default, but make sure
                  @match = $msg =~ /$args/i
                     or next topic;

               } elsif ($cmd eq "cond") {
                  cf::safe_eval $args, %vars
                     or next topic;

               } elsif ($cmd eq "eval") {
                  cf::safe_eval $args, %vars;
                  warn "\@eval evaluation error: $@\n" if $@;

               } elsif ($cmd eq "msg") {
                  push @replies, [$self->{npc}, (scalar cf::safe_eval $args, %vars)];

               } elsif ($cmd eq "setflag") {
                  my ($name, $value) = split /\s+/, $args, 2;
                  $value ? $flag->{$name} = $value
                         : delete $flag->{$name};

               } elsif ($cmd eq "setstate") {
                  my ($name, $value) = split /\s+/, $args, 2;
                  $value ? $state->{$name} = $value
                         : delete $state->{$name};

               } elsif ($cmd eq "ifflag") {
                  my ($name, $value) = split /\s+/, $args, 2;
                  $flag->{$name} eq $value
                     or next topic;

               } elsif ($cmd eq "ifstate") {
                  my ($name, $value) = split /\s+/, $args, 2;
                  $state->{$name} eq $value
                     or next topic;

               } elsif ($cmd eq "trigger") {
                  my $rvalue = \$self->{npc}{dialog_trigger}{$args*1};

                  my $trigger = cf::object::new "magic_ear";
                  $trigger->set_value ($$rvalue);
                  
                  # needs to be on the map for remove_button_link to work
                  # the same *should* be true for add_button_link....
                  $self->{npc}->map->insert_object ($trigger, 0, 0);

                  $trigger->add_button_link ($self->{npc}->map, $args);

                  $trigger->use_trigger;

                  $trigger->remove_button_link;
                  $trigger->remove;
                  $trigger->free;

                  $$rvalue = !$$rvalue;

               } elsif ($cmd eq "addtopic") {
                  push @kw, split /\|/, $args;
                  $self->{add_topic}->(split /\s*\|\s*/, $args) if $self->{add_topic};

               } elsif ($cmd eq "deltopic") {
                  # not yet implemented, do it out-of-band
                  $self->{del_topic}->(split /\s*\|\s*/, $args) if $self->{del_topic};

               } else {
                  warn "unknown dialogue command <$cmd,$args> used (from " . $self->{npc}->get_message . ")";
               }
            }

            delete $self->{npc}{$self->{ob}->name}{dialog_state} unless %$state;
            delete $self->{ob}{dialog_flag}                      unless %$flag;

            # combine lines into paragraphs
            $reply =~ s/(?<=\S)\n(?=\w)/ /g;
            $reply =~ s/\n\n/\n/g;

            # ignores flags and npc from replies
            $reply = join "\n", (map $_->[1], @replies), $reply;

            # now mark up all matching keywords
            for my $match (@{ $self->{match} }) {
               for (sort { (length $b) <=> (length $a) } split /\|/, $match->[0]) {
                  if ($reply =~ /\b\Q$_\E\b/i) {
                     push @kw, $_;
                     last;
                  }
               }
            }

            return wantarray ? ($reply, @kw) : $reply;
         }
      }
   }

   ()
}

