<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/*[local-name()='people']">
        <html>
            <body>
                <xsl:apply-templates select="person"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="person">
        <xsl:if test="(father) and (mother) and ((id(father/@id)/father) or (id(mother/@id)/father) or (id(father/@id)/mother) or (id(mother/@id)/mother)) and ((brothers/brother) or (sisters/sister))">
            <table border="1">
                <tr>
                    <th>Relationship</th>
                    <th>Name</th>
                    <th>Gender</th>
                    <th>Father</th>
                    <th>Mother</th>
                    <th>Brothers</th>
                    <th>Sisters</th>
                    <th>Sons</th>
                    <th>Daughters</th>
                    <th>Grandfathers</th>
                    <th>Grandmothers</th>
                    <th>Uncles</th>
                    <th>Aunts</th>
                </tr>
                <tr>
                    <td/>
                    <xsl:call-template name="person-info">
                        <xsl:with-param name="person" select="."/>
                    </xsl:call-template>
                </tr>
                <tr>
                    <td>Father</td>
                    <xsl:call-template name="person-info">
                        <xsl:with-param name="person" select="id(father/@id)"/>
                    </xsl:call-template>
                </tr>
                <tr>
                    <td>Mother</td>
                    <xsl:call-template name="person-info">
                        <xsl:with-param name="person" select="id(mother/@id)"/>
                    </xsl:call-template>
                </tr>
                <xsl:for-each select="id(brothers/brother/@id)">
                    <tr>
                        <td>Brother</td>
                        <xsl:call-template name="person-info">
                            <xsl:with-param name="person" select="."/>
                        </xsl:call-template>
                    </tr>
                </xsl:for-each>
                <xsl:for-each select="id(sisters/sister/@id)">
                    <tr>
                        <td>Sister</td>
                        <xsl:call-template name="person-info">
                            <xsl:with-param name="person" select="."/>
                        </xsl:call-template>
                    </tr>
                </xsl:for-each>
            </table>

            <br/>

        </xsl:if>
    </xsl:template>

    <xsl:template name="person-info">
        <xsl:param name="person"/>



        <td><xsl:value-of select="$person/@name"/> </td>
        <td><xsl:value-of select="$person/@gender"/></td>
        <td><xsl:value-of select="id($person/father/@id)/@name"/></td>
        <td><xsl:value-of select="id($person/mother/@id)/@name"/></td>
        <td>
            <xsl:for-each select="id($person/brothers/brother/@id)">
                <xsl:value-of select="@name"/><p/>
            </xsl:for-each>
        </td>
        <td>
            <xsl:for-each select="id($person/sisters/sister/@id)">
                <xsl:value-of select="@name"/><p/>
            </xsl:for-each>
        </td>
        <td>
            <xsl:for-each select="id($person/sons/son/@id)">
                <xsl:value-of select="@name"/><p/>
            </xsl:for-each>
        </td>
        <td>
            <xsl:for-each select="id($person/daughters/daughter/@id)">
                <xsl:value-of select="@name"/><p/>
            </xsl:for-each>
        </td>
        <td>
            <xsl:if test="id($person/father/@id)/father">
                <xsl:value-of select="id(id($person/father/@id)/father/@id)/@name"/>
                <p/>
            </xsl:if>
            <xsl:if test="id($person/mother/@id)/father">
                <xsl:value-of select="id(id($person/mother/@id)/father/@id)/@name"/>
            </xsl:if>
        </td>
        <td>
            <xsl:if test="id($person/father/@id)/mother">
                <xsl:value-of select="id(id($person/father/@id)/mother/@id)/@name"/>
                <p/>
            </xsl:if>
            <xsl:if test="id($person/mother/@id)/mother">
                <xsl:value-of select="id(id($person/mother/@id)/mother/@id)/@name"/>
            </xsl:if>
        </td>
        <td>
            <xsl:for-each select="id(id($person/father/@id)/brothers/brother/@id)">
                <xsl:value-of select="@name"/><p/>
            </xsl:for-each>
            <xsl:for-each select="id(id($person/mother/@id)/brothers/brother/@id)">
                <xsl:value-of select="@name"/><p/>
            </xsl:for-each>
        </td>
        <td>
            <xsl:for-each select="id(id($person/father/@id)/sisters/sister/@id)">
                <xsl:value-of select="@name"/><p/>
            </xsl:for-each>
            <xsl:for-each select="id(id($person/mother/@id)/sisters/sister/@id)">
                <xsl:value-of select="@name"/><p/>
            </xsl:for-each>
        </td>
    </xsl:template>
</xsl:stylesheet>
