package org.mpierce.staxmate;

import com.ctc.wstx.stax.WstxInputFactory;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.nio.charset.Charset;

public final class StaxMateParseTest {

    @Test
    public void testParseWithStreamReader() throws XMLStreamException {
        SMInputFactory inf = new SMInputFactory(new WstxInputFactory());

        SMHierarchicCursor rootElCursor =
            inf.rootElementCursor(this.getClass().getResourceAsStream("sample.xml"));
        rootElCursor.advance();

        SMInputCursor rootChildCursor = rootElCursor.childElementCursor();

        while (rootChildCursor.getNext() != null) {
            SMInputCursor grandChildCursor;
            switch (rootChildCursor.getLocalName()) {
                case "child1":
                    grandChildCursor = rootChildCursor.childElementCursor();

                    while (grandChildCursor.getNext() != null) {
                        System.out.println("Got " + grandChildCursor.getElemStringValue());
                    }

                    break;
                case "child2":

                    grandChildCursor = rootChildCursor.childElementCursor();

                    while (grandChildCursor.getNext() != null) {
                        System.out.println("Local name " + grandChildCursor.getLocalName());
                        switch (grandChildCursor.getLocalName()) {
                            case "baz":
                                System.out.println("Got baz: " + grandChildCursor.getElemStringValue());
                                break;
                            case "base64":
                                // should be 'hello world'
                                System.out.println("Got base64: " +
                                    new String(grandChildCursor.getStreamReader().getElementAsBinary(), Charset.forName(
                                        "UTF-8")));
                                break;
                            case "quux":
                                // Why isn't this hit?
                                System.out.println("Got quux: " + grandChildCursor.getElemStringValue());
                                break;
                        }
                    }

                    break;
                case "child3":
                    grandChildCursor = rootChildCursor.childElementCursor();

                    while (grandChildCursor.getNext() != null) {
                        System.out.println("Got " + grandChildCursor.getElemStringValue());
                    }
                    break;
            }
        }
    }
}
