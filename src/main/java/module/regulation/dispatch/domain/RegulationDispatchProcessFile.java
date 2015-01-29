/*
 * @(#)RegulationDispatchProcessFile.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Anil Kassamali
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Dispatch Registry Module.
 *
 *   The Dispatch Registry Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Dispatch Registry Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Dispatch Registry Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.regulation.dispatch.domain;


/**
 * 
 * @author Anil Kassamali
 * 
 */
public class RegulationDispatchProcessFile extends RegulationDispatchProcessFile_Base {

    private RegulationDispatchProcessFile(final RegulationDispatchWorkflowMetaProcess process, final String displayName,
            final String filename, final byte[] content) {
        super();
        init(displayName, filename, content);
        RegulationDispatchProcessFile mainDocument = process.getMainDocument();

        activate();
        setMainDocument(mainDocument == null);
        process.addFiles(this);
    }

    public static RegulationDispatchProcessFile create(final RegulationDispatchWorkflowMetaProcess process,
            final String displayName, final String filename, final byte[] content) {
        return new RegulationDispatchProcessFile(process, displayName, filename, content);
    }

    public void activate() {
        setActive(true);
    }

    public void deactivate() {
        setActive(false);
    }

    @Override
    public RegulationDispatchWorkflowMetaProcess getProcess() {
        return (RegulationDispatchWorkflowMetaProcess) super.getProcess();
    }

    public void setAsMainDocument() {
        RegulationDispatchProcessFile currentMainDocument = getProcess().getMainDocument();
        currentMainDocument.setMainDocument(false);
        setMainDocument(true);
    }

    public boolean isAbleToRemove() {
        return getActive() && !getMainDocument();
    }

    public boolean isAbleToSetAsMainDocument() {
        return getActive() && !getMainDocument();
    }

}
