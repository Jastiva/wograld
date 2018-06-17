/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.sf.gridarta.model.validation.checks;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.MapWriter;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.validation.NoSuchValidatorException;
import net.sf.gridarta.model.validation.Validator;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.utils.StringParameterBuilder;
import net.sf.gridarta.utils.StringUtils;
import net.sf.gridarta.utils.SyntaxErrorException;
import org.jetbrains.annotations.NotNull;

/**
 * A factory for creating {@link Validator} instances from string
 * representation. The string representation consists of a class name (which
 * must implement {@link Validator}) and optional constructor arguments.
 * @author Andreas Kirschbaum
 */
public class ValidatorFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link ValidatorPreferences} to pass to the newly created {@link
     * Validator} instances.
     */
    @NotNull
    private final ValidatorPreferences validatorPreferences;

    /**
     * The {@link GameObjectMatchers} for looking up {@link GameObjectMatcher}
     * instances from string representation.
     */
    @NotNull
    private final GameObjectMatchers gameObjectMatchers;

    /**
     * The {@link GlobalSettings} to pass to the newly created {@link Validator}
     * instances.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link MapWriter} to pass to the newly created {@link Validator}
     * instances.
     */
    @NotNull
    private final MapWriter<G, A, R> mapWriter;

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to pass to the
     * newly created validator instances
     * @param gameObjectMatchers the game object matchers for looking up game
     * object matcher instances from string representation
     * @param globalSettings the global settings to pass to the newly created
     * validator instances
     * @param mapWriter the map writer to pass to the newly created validator
     * instances
     */
    public ValidatorFactory(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final GameObjectMatchers gameObjectMatchers, @NotNull final GlobalSettings globalSettings, @NotNull final MapWriter<G, A, R> mapWriter) {
        this.validatorPreferences = validatorPreferences;
        this.gameObjectMatchers = gameObjectMatchers;
        this.globalSettings = globalSettings;
        this.mapWriter = mapWriter;
    }

    /**
     * Creates a new {@link Validator} instance from string representation.
     * @param spec the string representation
     * @return the validator instance
     * @throws NoSuchValidatorException if the validator cannot be created
     */
    @NotNull
    public Validator<G, A, R> newValidator(@NotNull final String spec) throws NoSuchValidatorException {
        final String[] args = StringUtils.PATTERN_WHITESPACE.split(spec);
        final Class<?> tmpClass;
        //noinspection ErrorNotRethrown
        try {
            tmpClass = Class.forName(args[0]);
        } catch (final ClassNotFoundException ex) {
            throw new NoSuchValidatorException("class " + args[0] + " does not exist", ex);
        } catch (final ExceptionInInitializerError ex) {
            throw new NoSuchValidatorException("class " + args[0] + " does not exist", ex);
        } catch (final LinkageError ex) {
            throw new NoSuchValidatorException("class " + args[0] + " does not exist", ex);
        }
        final Class<?> classValidator;
        try {
            classValidator = tmpClass.asSubclass(Validator.class);
        } catch (final ClassCastException ex) {
            throw new NoSuchValidatorException("class " + args[0] + " does not exist", ex);
        }
        final Constructor<?>[] constructors = classValidator.getConstructors();
        if (constructors.length == 0) {
            throw new NoSuchValidatorException("class " + args[0] + " has no public constructors");
        } else if (constructors.length > 1) {
            throw new NoSuchValidatorException("class " + args[0] + " has more than one public constructor");
        }
        //Class.getConstructors() did return the wrong type
        @SuppressWarnings("unchecked") final Constructor<? extends Validator<G, A, R>> constructor = (Constructor<? extends Validator<G, A, R>>) constructors[0];
        final Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
        final Object[] constructorArguments = new Object[constructorParameterTypes.length];
        int pos = 1;
        for (int i = 0; i < constructorParameterTypes.length; i++) {
            final Class<?> constructorParameterType = constructorParameterTypes[i];
            if (constructorParameterType == ValidatorPreferences.class) {
                constructorArguments[i] = validatorPreferences;
            } else if (constructorParameterType == GlobalSettings.class) {
                constructorArguments[i] = globalSettings;
            } else if (constructorParameterType == MapWriter.class) {
                constructorArguments[i] = mapWriter;
            } else {
                if (pos >= args.length) {
                    throw new NoSuchValidatorException("missing argument in '" + spec + "'");
                }
                final String arg = args[pos++];
                if (constructorParameterType == Integer[].class) {
                    constructorArguments[i] = createIntegerArray(arg);
                } else if (constructorParameterType == GameObjectMatcher.class) {
                    constructorArguments[i] = createGameObjectMatcher(arg);
                } else if (constructorParameterType == int.class) {
                    constructorArguments[i] = createInteger(arg);
                } else if (constructorParameterType == String[].class) {
                    constructorArguments[i] = createStringArray(args, pos - 1);
                    pos = args.length;
                } else if (constructorParameterType == Pattern.class) {
                    constructorArguments[i] = createPattern(arg);
                } else {
                    throw new NoSuchValidatorException("class " + args[0] + "'s constructor requires a parameter of type " + constructorParameterType.getName() + "; this type is not supported");
                }
            }
        }
        final Validator<G, A, R> validator;
        //noinspection ErrorNotRethrown
        try {
            validator = constructor.newInstance(constructorArguments);
        } catch (final ExceptionInInitializerError ex) {
            throw new NoSuchValidatorException("cannot initialize class " + args[0], ex);
        } catch (final InstantiationException ex) {
            throw new NoSuchValidatorException("class " + args[0] + " is abstract", ex);
        } catch (final IllegalAccessException ex) {
            throw new AssertionError(ex);
        } catch (final IllegalArgumentException ex) {
            throw new AssertionError(ex);
        } catch (final InvocationTargetException ex) {
            throw new NoSuchValidatorException("cannot instantiate class " + args[0], ex);
        }
        if (pos < args.length) {
            if (classValidator == UnsetSlayingChecker.class) {
                final UnsetSlayingChecker<?, ?, ?> unsetSlayingChecker = (UnsetSlayingChecker<?, ?, ?>) validator;
                do {
                    unsetSlayingChecker.addAllowedValue(args[pos]);
                    pos++;
                } while (pos < args.length);
            } else if (classValidator == CustomTypeChecker.class) {
                final CustomTypeChecker<?, ?, ?> customTypeChecker = (CustomTypeChecker<?, ?, ?>) validator;
                do {
                    final String[] tmp = StringUtils.PATTERN_COMMA.split(args[pos], -1);
                    if (tmp.length != 2 && tmp.length != 3) {
                        throw new NoSuchValidatorException("invalid from,to or from,to,env type: " + args[pos]);
                    }
                    final int fromType;
                    try {
                        fromType = Integer.parseInt(tmp[0]);
                    } catch (final NumberFormatException ex) {
                        throw new NoSuchValidatorException("invalid from type: " + args[pos], ex);
                    }
                    final int toType;
                    try {
                        toType = Integer.parseInt(tmp[1]);
                    } catch (final NumberFormatException ex) {
                        throw new NoSuchValidatorException("invalid to type: " + args[pos], ex);
                    }
                    if (tmp.length == 2) {
                        customTypeChecker.addIgnore(fromType, toType);
                    } else {
                        final int envType;
                        try {
                            envType = Integer.parseInt(tmp[2]);
                        } catch (final NumberFormatException ex) {
                            throw new NoSuchValidatorException("invalid env type: " + args[pos], ex);
                        }
                        customTypeChecker.addIgnore(fromType, toType, envType);
                    }
                    pos++;
                } while (pos < args.length);
            } else if (classValidator == SlayingChecker.class) {
                final SlayingChecker<?, ?, ?> slayingChecker = (SlayingChecker<?, ?, ?>) validator;
                do {
                    final String[] tmp = StringUtils.PATTERN_COMMA.split(args[pos], 2);
                    if (tmp.length != 2) {
                        throw new NoSuchValidatorException("invalid matcher,pattern: " + args[pos]);
                    }
                    final GameObjectMatcher matcher = createGameObjectMatcher(tmp[0]);
                    final Pattern pattern = createPattern(tmp[1]);
                    slayingChecker.addMatcher(matcher, pattern);
                    pos++;
                } while (pos < args.length);
            } else {
                throw new NoSuchValidatorException("excess arguments for '" + spec + "'");
            }
        }
        return validator;
    }

    /**
     * Creates an <code>Integer[]</code> instance from string representation.
     * @param arg the string representation
     * @return the integer array instance
     * @throws NoSuchValidatorException if the string representation is
     * incorrect
     */
    @NotNull
    private static Integer[] createIntegerArray(@NotNull final CharSequence arg) throws NoSuchValidatorException {
        final String[] tmp = StringUtils.PATTERN_COMMA.split(arg, -1);
        final Integer[] result = new Integer[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            try {
                result[i] = Integer.parseInt(tmp[i]);
            } catch (final NumberFormatException ex) {
                throw new NoSuchValidatorException("not a number: " + tmp[i], ex);
            }
        }
        return result;
    }

    /**
     * Creates an <code>Integer[]</code> instance from string representation.
     * @param args the string representation
     * @param pos the first element of <code>array</code> to use
     * @return the integer array instance
     * @throws NoSuchValidatorException if the string representation is
     * incorrect
     */
    @NotNull
    private String[] createStringArray(@NotNull final String[] args, final int pos) throws NoSuchValidatorException {
        final StringParameterBuilder stringParameterBuilder = new StringParameterBuilder();
        stringParameterBuilder.addParameter("COLLECTED", globalSettings.getCollectedDirectory().getPath());
        stringParameterBuilder.addParameter("ARCH", globalSettings.getArchDirectory().getPath());
        stringParameterBuilder.addParameter("MAPS", globalSettings.getMapsDirectory().getPath());
        stringParameterBuilder.addParameter("MAP", MapCheckerScriptChecker.MAP_PLACEHOLDER);
        final String[] result = new String[args.length - pos];
        for (int i = 0; i < result.length; i++) {
            try {
                result[i] = stringParameterBuilder.replace(args[pos + i]);
            } catch (final SyntaxErrorException ex) {
                throw new NoSuchValidatorException(ex.getMessage(), ex);
            }
        }
        return result;
    }

    /**
     * Creates a {@link GameObjectMatcher} instance from string representation.
     * @param arg the string representation
     * @return the game object matcher instance
     * @throws NoSuchValidatorException if the string representation is
     * incorrect
     */
    @NotNull
    private GameObjectMatcher createGameObjectMatcher(@NotNull final String arg) throws NoSuchValidatorException {
        final GameObjectMatcher gameObjectMatcher = gameObjectMatchers.getMatcher(arg);
        if (gameObjectMatcher == null) {
            throw new NoSuchValidatorException("undefined game object matcher: " + arg);
        }
        return gameObjectMatcher;
    }

    /**
     * Creates an <code>Integer</code> instance from string representation.
     * @param arg the string representation
     * @return the integer instance
     * @throws NoSuchValidatorException if the string representation is
     * incorrect
     */
    @NotNull
    private static Integer createInteger(@NotNull final String arg) throws NoSuchValidatorException {
        try {
            return Integer.parseInt(arg);
        } catch (final NumberFormatException ex) {
            throw new NoSuchValidatorException("invalid number: " + arg, ex);
        }
    }

    /**
     * Creates an <code>Integer</code> instance from string representation.
     * @param arg the string representation
     * @return the integer instance
     * @throws NoSuchValidatorException if the string representation is
     * incorrect
     */
    @NotNull
    private static Pattern createPattern(@NotNull final String arg) throws NoSuchValidatorException {
        try {
            return Pattern.compile(arg);
        } catch (final PatternSyntaxException ex) {
            throw new NoSuchValidatorException("invalid pattern: " + arg, ex);
        }
    }

}
