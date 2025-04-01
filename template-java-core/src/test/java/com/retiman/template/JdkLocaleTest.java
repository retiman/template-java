package com.retiman.template;

import java.util.IllformedLocaleException;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


@SuppressWarnings("deprecation")
public final class JdkLocaleTest {
  @Test
  public void testStringRepresentations() {
    Locale locale = new Locale.Builder()
        .setLanguage("zh")
        .setScript("Hans")
        .setRegion("CN")
        .setVariant("wadegile")
        .setExtension('t', "en")
        .setExtension('u', "latn")
        .setExtension('x', "apex")
        .build();

    // JDK locale toString method is designed for debuggability and not intended to be any type of standard.  It's not
    // possible to go from this string representation to a locale or a language tag.
    //
    // The JDK documentation states that "clients who parse the output of toString into language, country, and variant
    // fields can continue to do so (although this is strongly discouraged), although the variant field will have
    // additional information in it if script or extensions are present."
    //
    // Parsing the toString output of locales with scripts is not advised.
    //
    // See https://docs.oracle.com/javase/7/docs/api/java/util/Locale.html
    assertThat(locale.toString()).isEqualTo("zh_CN_wadegile_#Hans_t-en-u-latn-x-apex");

    // The JDK locale class will not have information about display variants (in this case).
    assertThat(locale.getVariant()).isEqualTo("wadegile");
    assertThat(locale.getDisplayVariant()).isEqualTo("wadegile");

    // The IETF language tag; however, can be used to reconstruct a locale.
    String tag = "zh-Hans-CN-wadegile-t-en-u-latn-x-apex";
    assertThat(locale.toLanguageTag()).isEqualTo(tag);
    assertThat(Locale.forLanguageTag(tag)).isEqualTo(locale);
  }

  @Test
  public void testEquivalentLegacyCodes() {
    Locale iw = new Locale("iw");
    Locale he = new Locale("he");

    // In older versions of Java, the JDK locale class conveniently converts the ISO-639 code for "he" (Hebrew) to "iw"
    // for you.  The "iw" code was changed in 1989 to "he", so it is considered the legacy code, but the JDK was big on
    // backwards compatibility for this matter.  This is no longer true in OpenJDK 17.
    //
    // The toString method and getLanguage method will always give you back the OLD value, no matter what.
    assertThat(iw.toString()).isEqualTo("he");
    assertThat(he.toString()).isEqualTo("he");
    assertThat(iw.getLanguage()).isEqualTo("he");
    assertThat(he.getLanguage()).isEqualTo("he");

    // However, the "iw" language tag isn't valid, so you'll always get the "he" tag back.
    assertThat(iw.toLanguageTag()).isEqualTo("he");
    assertThat(he.toLanguageTag()).isEqualTo("he");

    // The two locales are considered equivalent though.
    assertThat(iw).isEqualTo(he);

    // The JDK docs recommend that you do this for comparisons:
    assertThat(iw.getLanguage()).isEqualTo(he.getLanguage());
  }

  @Test
  public void testLanguageCodes() {
    // A list of ISO-639 codes can be found here: https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
    Locale afrikaans2c = new Locale("af");
    Locale afrikaans3c = new Locale("afr");

    assertThat(afrikaans2c.toString()).isEqualTo("af");
    assertThat(afrikaans2c.toLanguageTag()).isEqualTo("af");
    assertThat(afrikaans3c.toString()).isEqualTo("afr");
    assertThat(afrikaans3c.toLanguageTag()).isEqualTo("afr");
    assertThat(afrikaans2c).isNotEqualTo(afrikaans3c);
  }

  @Test
  public void testLanguageCodesNormalized() {
    Locale lower = new Locale("en");
    Locale upper = new Locale("EN");

    assertThat(lower.getLanguage()).isEqualTo("en");
    assertThat(upper.getLanguage()).isEqualTo("en");
    assertThat(lower.toLanguageTag()).isEqualTo("en");
    assertThat(upper.toLanguageTag()).isEqualTo("en");
    assertThat(lower).isEqualTo(upper);
  }

  @Test
  public void testInvalidLanguageCodes() {
    // A list of ISO-639 codes can be found here: https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
    Locale locale = new Locale("abcd");

    assertThat(locale.toLanguageTag()).isEqualTo("abcd");
  }

  @Test
  public void testInvalidTooShortScript() {
    Throwable thrown = catchThrowable(() -> new Locale.Builder()
        .setLanguage("zh")
        .setScript("Han")
        .build());

    assertThat(thrown).isExactlyInstanceOf(IllformedLocaleException.class);
  }

  @Test
  public void testInvalidScriptForLanguage() {
    Locale locale = new Locale.Builder()
        .setLanguage("en")
        .setScript("Hant")
        .build();

    assertThat(locale.toString()).isEqualTo("en__#Hant");
    assertThat(locale.toLanguageTag()).isEqualTo("en-Hant");
  }

  @Test
  public void testRegionCodes() {
    // A list of ISO-3661 codes can be found here: https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
    Locale franceFr = new Locale("fr", "FR");
    Locale france250 = new Locale("fr", "250");

    assertThat(franceFr.toLanguageTag()).isEqualTo("fr-FR");
    assertThat(france250.toLanguageTag()).isEqualTo("fr-250");
    assertThat(franceFr).isNotEqualTo(france250);
  }

  @Test
  public void testRegionCodesNormalized() {
    Locale lower = new Locale("fr", "fr");
    Locale upper = new Locale("fr", "FR");

    assertThat(lower.getCountry()).isEqualTo("FR");
    assertThat(upper.getCountry()).isEqualTo("FR");
    assertThat(lower.toLanguageTag()).isEqualTo("fr-FR");
    assertThat(upper.toLanguageTag()).isEqualTo("fr-FR");
    assertThat(lower).isEqualTo(upper);
  }

  @Test
  public void testVariantsAreLowercased() {
    Locale locale = new Locale("en", "US", "variant");

    assertThat(locale.getVariant()).isEqualTo("variant");
    assertThat(locale.toLanguageTag()).isEqualTo("en-US-variant");
  }

  @Test
  public void testInvalidShortVariantsAreConvertedToExtensions() {
    Locale locale = new Locale("en", "US", "1");

    // The toString value gives the variant value as is.  Since the toString value is not part of any specification,
    // the variant part can be as many characters as you want.
    assertThat(locale.toString()).isEqualTo("en_US_1");

    // Variants must be between 5-8 characters, if they are too short, they are converted to extensions.
    assertThat(locale.getVariant()).isEqualTo("1");
    assertThat(locale.toLanguageTag()).isEqualTo("en-US-x-lvariant-1");

    // They can be converted back to Locales as well.
    Locale converted = Locale.forLanguageTag("en-US-x-lvariant-1");

    assertThat(converted.getVariant()).isEqualTo("1");

    Locale extension = new Locale.Builder()
        .setLanguage("en")
        .setRegion("US")
        .setExtension('x', "lvariant-1")
        .build();
    assertThat(extension).isEqualTo(converted);
  }

  @Test
  public void testInvalidLongVariantsAreDiscarded() {
    Locale locale = new Locale("en", "US", "thisvariantistoolong");

    // The toString value gives the variant value as is.  Since the toString value is not part of any specification,
    // the variant part can be as many characters as you want.
    assertThat(locale.toString()).isEqualTo("en_US_thisvariantistoolong");

    // Variants must be between 5-8 characters, if they are too long, they are dropped.  This is because extensions must
    // be between 2-8 characters, and separated by hyphens.  Converting to an extension would result in a too long
    // extension, and instead the variant is dropped when creating a language tag.
    assertThat(locale.getVariant()).isEqualTo("thisvariantistoolong");
    assertThat(locale.toLanguageTag()).isEqualTo("en-US");
  }

  @Test
  public void testInvalidExtensionsThatAreTooShort() {
    Locale locale = new Locale.Builder()
        .setLanguage("en")
        .setExtension('x', "1")
        .build();

    assertThat(locale.toString()).isEqualTo("en__#x-1");
    assertThat(locale.toLanguageTag()).isEqualTo("en-x-1");

    locale = new Locale.Builder()
        .setLanguage("en")
        .setExtension('x', "")
        .build();

    assertThat(locale.toString()).isEqualTo("en");
    assertThat(locale.toLanguageTag()).isEqualTo("en");
  }

  @Test
  public void testInvalidExtensionsThatAreTooLongAreIllformed() {
    Throwable thrown = catchThrowable(() -> new Locale.Builder()
        .setLanguage("en")
        .setExtension('x', "thisextensionistoolong")
        .build());

    assertThat(thrown).isExactlyInstanceOf(IllformedLocaleException.class);
  }
}
