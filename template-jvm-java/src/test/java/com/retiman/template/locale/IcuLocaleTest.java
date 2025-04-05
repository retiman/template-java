package com.retiman.template.locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.ibm.icu.util.IllformedLocaleException;
import com.ibm.icu.util.ULocale;
import java.util.Locale;
import org.junit.jupiter.api.Test;

public final class IcuLocaleTest {
  @Test
  public void testStringRepresentations() {
    ULocale locale =
        new ULocale.Builder()
            .setLanguage("zh")
            .setScript("Hans")
            .setRegion("CN")
            .setVariant("wadegile")
            .setExtension('t', "en")
            .setExtension('u', "latn")
            .setExtension('x', "apex")
            .build();

    // The ICU locale toString method is designed for debuggability and not intended to be any type
    // of standard.  It's
    // not possible to go from this string representation to a locale or a language tag.  Notably it
    // is also different
    // from the JDK locale's toString representation.
    //
    // The script added after the language code to more closely mirror the IETF language tag.
    assertThat(locale.toString()).isEqualTo("zh_Hans_CN_WADEGILE@attribute=latn;t=en;x=apex");

    // An interesting thing to note is that ICU locales will uppercase the variant; however, it gets
    // the display variant
    // correct.  It seems that the JDK version of the ICU library will uppercase no matter what.
    // While the script and
    // region subtags must be title case and uppercase, respectively, no requirement is given to
    // variant subtags.
    //
    // That said, the recommendation is that variant subtags are lowercase.  This is specified in
    // the ICU documentation
    // about case normalization.  Not sure why, but ok.
    assertThat(locale.getVariant()).isEqualTo("WADEGILE");
    assertThat(locale.getDisplayVariant()).isEqualTo("Wade-Giles Romanization");

    // The IETF language tag; however, can be used to reconstruct a locale; it will agree with the
    // JDK's representation
    // of the language tag.  In spite of the case normalization applied to variants, you will get
    // the correct IETF
    // language tag (with a lowercased variant) if you ask for it.
    String tag = "zh-Hans-CN-wadegile-t-en-u-latn-x-apex";
    assertThat(locale.toLanguageTag()).isEqualTo(tag);
    assertThat(ULocale.forLanguageTag(tag)).isEqualTo(locale);

    // Unfortunately, though, if you try to convert this corresponding ULocale to a JDK locale, the
    // variant will remain
    // uppercased.
    Locale jdkLocale = Locale.forLanguageTag(tag);
    ULocale icuLocale = ULocale.forLanguageTag(tag);
    assertThat(jdkLocale).isNotEqualTo(icuLocale);

    // To construct a corresponding JDK locale, the variant must be lowercased.
    Locale transformed =
        new Locale.Builder()
            .setLocale(icuLocale.toLocale())
            .setVariant(icuLocale.getVariant().toLowerCase(Locale.US))
            .build();
    assertThat(jdkLocale).isEqualTo(transformed);
  }

  @Test
  public void testNonEquivalentLegacyCodes() {
    ULocale iw = new ULocale("iw");
    ULocale he = new ULocale("he");

    // The ICU locale class is mostly WYSIWYG.  If you asked for "iw", you get "iw".  If you asked
    // for "he", you'll get
    // "he".
    assertThat(iw.toString()).isEqualTo("iw");
    assertThat(he.toString()).isEqualTo("he");
    assertThat(iw.getLanguage()).isEqualTo("iw");
    assertThat(he.getLanguage()).isEqualTo("he");

    // The only difference is that the IETF language tags must be valid, so you'll get the same
    // behavior as the JDK
    // locale.
    assertThat(iw.toLanguageTag()).isEqualTo("he");
    assertThat(he.toLanguageTag()).isEqualTo("he");

    // In fact, the two locales are not even considered equivalent.
    assertThat(iw).isNotEqualTo(he);

    // Their language tags are though.
    assertThat(iw.toLanguageTag()).isEqualTo(he.toLanguageTag());
  }

  @Test
  public void testLanguageCodes() {
    // A list of ISO-639 codes can be found here:
    // https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
    ULocale afrikaans2c = new ULocale("af");
    ULocale afrikaans3c = new ULocale("afr");

    assertThat(afrikaans2c.toString()).isEqualTo("af");
    assertThat(afrikaans2c.toLanguageTag()).isEqualTo("af");

    // In contrast to the JDK locale class, proper normalizations and canonicalizations will be
    // applied.  For "afr",
    // there is no ambiguity between the "af" code and "afr", so "af" is the normalziation.
    assertThat(afrikaans3c.toString()).isEqualTo("af");
    assertThat(afrikaans3c.getLanguage()).isEqualTo("af");
    assertThat(afrikaans3c.toLanguageTag()).isEqualTo("af");

    // They are also considered the same locale.
    assertThat(afrikaans2c).isEqualTo(afrikaans3c);
  }

  @Test
  public void testLanguageCodesNormalized() {
    ULocale lower = new ULocale("en");
    ULocale upper = new ULocale("EN");

    assertThat(lower.getLanguage()).isEqualTo("en");
    assertThat(upper.getLanguage()).isEqualTo("en");
    assertThat(lower.toLanguageTag()).isEqualTo("en");
    assertThat(upper.toLanguageTag()).isEqualTo("en");
    assertThat(lower).isEqualTo(upper);
  }

  @Test
  public void testInvalidLanguageCodes() {
    // A list of ISO-639 codes can be found here:
    // https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
    ULocale locale = new ULocale("abcd");

    assertThat(locale.toLanguageTag()).isEqualTo("abcd");
  }

  @Test
  public void testInvalidTooShortScript() {
    Throwable thrown =
        catchThrowable(() -> new ULocale.Builder().setLanguage("zh").setScript("Han").build());

    assertThat(thrown).isExactlyInstanceOf(IllformedLocaleException.class);
  }

  @Test
  public void testInvalidScriptForLanguage() {
    ULocale locale = new ULocale.Builder().setLanguage("en").setScript("Hant").build();

    assertThat(locale.toString()).isEqualTo("en_Hant");
    assertThat(locale.toLanguageTag()).isEqualTo("en-Hant");
  }

  @Test
  public void testRegionCodes() {
    // A list of ISO-3661 codes can be found here: https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
    ULocale franceFr = new ULocale("fr", "FR");
    ULocale france250 = new ULocale("fr", "250");

    assertThat(franceFr.toLanguageTag()).isEqualTo("fr-FR");
    assertThat(france250.toLanguageTag()).isEqualTo("fr-250");
    assertThat(franceFr).isNotEqualTo(france250);
  }

  @Test
  public void testRegionCodesNormalized() {
    ULocale lower = new ULocale("fr", "fr");
    ULocale upper = new ULocale("fr", "FR");

    assertThat(lower.getCountry()).isEqualTo("FR");
    assertThat(upper.getCountry()).isEqualTo("FR");
    assertThat(lower.toLanguageTag()).isEqualTo("fr-FR");
    assertThat(upper.toLanguageTag()).isEqualTo("fr-FR");
    assertThat(lower).isEqualTo(upper);
  }

  @Test
  public void testVariantsAreUppercasedExceptInLanguageTags() {
    ULocale locale = new ULocale("en", "US", "variant");

    assertThat(locale.getVariant()).isEqualTo("VARIANT");
    assertThat(locale.toLanguageTag()).isEqualTo("en-US-variant");
  }

  @Test
  public void testInvalidShortVariantsAreConvertedToExtensions() {
    ULocale locale = new ULocale("en", "US", "1");

    // The toString value gives the variant value as is.  Since the toString value is not part of
    // any specification,
    // the variant part can be as many characters as you want.
    assertThat(locale.toString()).isEqualTo("en_US_1");

    // Variants must be between 5-8 characters, if they are too short, they are converted to
    // extensions.
    assertThat(locale.getVariant()).isEqualTo("1");
    assertThat(locale.toLanguageTag()).isEqualTo("en-US-x-lvariant-1");

    // They can be converted back to ULocales as well.
    ULocale converted = ULocale.forLanguageTag("en-US-x-lvariant-1");

    assertThat(converted.getVariant()).isEqualTo("1");

    ULocale extension =
        new ULocale.Builder()
            .setLanguage("en")
            .setRegion("US")
            .setExtension('x', "lvariant-1")
            .build();
    assertThat(extension).isEqualTo(converted);
  }

  @Test
  public void testInvalidLongVariantsAreDiscarded() {
    ULocale locale = new ULocale("en", "US", "thisvariantistoolong");

    // The toString value gives the variant value as is.  Since the toString value is not part of
    // any specification,
    // the variant part can be as many characters as you want.
    assertThat(locale.toString()).isEqualTo("en_US_THISVARIANTISTOOLONG");

    // Variants must be between 5-8 characters, if they are too long, they are dropped.  This is
    // because extensions must
    // be between 2-8 characters, and separated by hyphens.  Converting to an extension would result
    // in a too long
    // extension, and instead the variant is dropped when creating a language tag.
    assertThat(locale.getVariant()).isEqualTo("THISVARIANTISTOOLONG");
    assertThat(locale.toLanguageTag()).isEqualTo("en-US");
  }

  @Test
  public void testInvalidExtensionsThatAreTooShort() {
    ULocale locale = new ULocale.Builder().setLanguage("en").setExtension('x', "1").build();

    assertThat(locale.toString()).isEqualTo("en@x=1");
    assertThat(locale.toLanguageTag()).isEqualTo("en-x-1");

    locale = new ULocale.Builder().setLanguage("en").setExtension('x', "").build();

    assertThat(locale.toString()).isEqualTo("en");
    assertThat(locale.toLanguageTag()).isEqualTo("en");
  }

  @Test
  public void testInvalidExtensionsThatAreTooLongAreIllformed() {
    Throwable thrown =
        catchThrowable(
            () ->
                new ULocale.Builder()
                    .setLanguage("en")
                    .setExtension('x', "thisextensionistoolong")
                    .build());

    assertThat(thrown).isExactlyInstanceOf(IllformedLocaleException.class);
  }
}
