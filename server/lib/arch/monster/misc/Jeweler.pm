=head1 NAME

Jeweler

=head1 DESCRIPTION

The Jeweler skill helper module.

=cut

package Jeweler;

=over 4

=item @RESISTS

List of all resistancies that can occur on rings and amulets.

=cut

my @RESISTS = (
   cf::ATNR_PHYSICAL,
   cf::ATNR_MAGIC,
   cf::ATNR_FIRE,
   cf::ATNR_ELECTRICITY,
   cf::ATNR_COLD,
   cf::ATNR_CONFUSION,

   cf::ATNR_ACID,
   cf::ATNR_DRAIN,
   cf::ATNR_GHOSTHIT,
   cf::ATNR_POISON,
   cf::ATNR_SLOW,
   cf::ATNR_PARALYZE,

   cf::ATNR_TURN_UNDEAD,
   cf::ATNR_FEAR,
   cf::ATNR_DEPLETE,
   cf::ATNR_DEATH,
   cf::ATNR_HOLYWORD,
   cf::ATNR_LIFE_STEALING,

   cf::ATNR_BLIND,
   cf::ATNR_DISEASE,
);

=item @EFFECT_RESISTS

List of all effect resistancies that occur on rings and amulets.
The difference is made because effect resistancies are less effective at lower levels.

=cut

my @EFFECT_RESISTS = (
   cf::ATNR_CONFUSION,
   cf::ATNR_DRAIN,
   cf::ATNR_POISON,
   cf::ATNR_SLOW,
   cf::ATNR_PARALYZE,
   cf::ATNR_TURN_UNDEAD,
   cf::ATNR_FEAR,
   cf::ATNR_DEPLETE,
   cf::ATNR_DEATH,
   cf::ATNR_BLIND,
   cf::ATNR_DISEASE,
);

my %RESMAP = (
   cf::ATNR_PHYSICAL => "PHYSICAL",
   cf::ATNR_MAGIC => "MAGIC",
   cf::ATNR_FIRE => "FIRE",
   cf::ATNR_ELECTRICITY => "ELECTRICITY",
   cf::ATNR_COLD => "COLD",
   cf::ATNR_CONFUSION => "CONFUSION",
   cf::ATNR_ACID => "ACID",

   cf::ATNR_DRAIN => "DRAIN",
   cf::ATNR_GHOSTHIT => "GHOSTHIT",
   cf::ATNR_POISON => "POISON",
   cf::ATNR_SLOW => "SLOW",
   cf::ATNR_PARALYZE => "PARALYZE",
   cf::ATNR_TURN_UNDEAD => "TURN_UNDEAD",

   cf::ATNR_FEAR => "FEAR",
   cf::ATNR_DEPLETE => "DEPLETE",
   cf::ATNR_DEATH => "DEATH",
   cf::ATNR_HOLYWORD => "HOLYWORD",
   cf::ATNR_LIFE_STEALING => "LIFE_STEALING",
   cf::ATNR_BLIND => "BLIND",
   cf::ATNR_DISEASE => "DISEASE",
);

=back

=cut

package Jeweler::CauldronHandler;

=head2 CauldronHandler

The Jeweler::CauldronHandler package, that helps you with handling the
cauldron stuff. Can also be used for other skills.

=cut

sub new {
   my ($class, %arg) = @_;

   my $self = bless {
      %arg,
   }, $class;

   $self;
}

=over 4

=item find_cauldron ($arch_name, @map_stack)

This finds the cauldron with C<$arch_name> on the C<@map_stack> and initalises the CauldronHandler.
It takes the topmost cauldron that is found. Returns undef if no cauldron was found.
Returns the cauldron object if it was found.

=cut

sub find_cauldron {
   my ($self, $arch_name, @map_stack) = @_;

   my @c =
      grep {
         $_->flag (cf::FLAG_IS_CAULDRON)
            and $_->archetype->name eq $arch_name
      } @map_stack;

   $self->{cauldron} = $c[0];
}

=item grep_by_type (@types)

Finds all objects in the cauldron that have the type of one of C<@types>.

=cut

sub grep_by_type {
   my ($self, @types) = @_;

   return () unless $self->{cauldron};

   my @res = grep {
      my $ob = $_;
      (grep { $ob->type == $_ } @types) > 0
   } $self->{cauldron}->inv;

   return @res
}

=item extract_jeweler_ingredients

Extracts the ingredients that matter for the Jeweler skill
and returns a Jeweler::Ingredients object.

=cut

sub extract_jeweler_ingredients {
   my ($self) = @_;

   return () unless $self->{cauldron};

   my $ingreds = {};

   my %type_to_key = (
         cf::RING      => 'rings',
         cf::AMULET    => 'ammys',
         cf::INORGANIC => 'mets_and_mins',
         cf::GEM       => 'gems',
         cf::POTION    => 'potions',
         cf::SCROLL    => 'scrolls',
   );

   for ($self->{cauldron}->inv) {

      if (my $k = $type_to_key{$_->type}) {
         push @{$ingreds->{$k}}, $_;

      } else {
         Jeweler::Util::remove ($_);
      }
   }

   return Jeweler::Ingredients->new (ingredients => $ingreds, cauldron_helper => $self)
}

=item put ($object)

Just puts the C<$object> into the cauldron.

=cut

sub put {
   my ($self, $obj) = @_;

   return undef unless $self->{cauldron};

   $obj->insert_ob_in_ob ($self->{cauldron});
}

=back

=cut

package Jeweler::Ingredients;

=head2 Ingredients

This class handles the ingredients.

=over 4

=item new (ingredients => $ingred_hash)

This is called from the CauldronHandler that gives you the ingredients.

=cut

sub new {
   my ($class, %arg) = @_;

   my $self = bless {
      %arg,
   }, $class;

   $self;
}

=item value ($group, $archname)

Returns the value of the ingredients in C<$group> with the archetypename C<$archname>.

=cut

sub value {
   my ($self, $group, $archname) = @_;

   my @objs = grep {
      $_->archetype->name eq $archname
   } @{$self->{ingredients}->{$group} || []};

   my $sum = 0;
   for (@objs) {
      $sum += $_->nrof * $_->value;
   }

   return $sum;
}

=item remove ($group, $archname)

Removes the ingredients in C<$group> with archname C<$archname>.

=cut

sub remove {
   my ($self, $group, $archname) = @_;

   my $ingred = $self->{ingredients};

   my @out;

   for (@{$ingred->{$group}}) {
      if ($_->archetype->name eq $archname) {
         Jeweler::Util::remove ($_);
      } else {
         push @out, $_;
      }
   }

   @{$ingred->{$grp}} = @out;
}

=back

=cut

package Jeweler::Util;

=head2 Util

Some utility functions for the Jeweler skill.

=over 4

=item remove ($object)

Removes the C<$object> and it's inventory recursivley from the game.

=cut

sub remove {
   my ($obj) = @_;

   remove ($_) for ($obj->inv);
   $obj->remove;
   $obj->free;
}

=back

=back

1

