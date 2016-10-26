/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.worldgrower.gui;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;

public class ImageTableRenderer extends DefaultTableCellRenderer {
    private final ImageInfoReader imageInfoReader;

    public ImageTableRenderer(ImageInfoReader imageInfoReader) {
		super();
		this.imageInfoReader = imageInfoReader;
	}

    @Override
	public void setValue(Object value) {
       ImageIds imageId = (ImageIds) value;
       setIcon(new ImageIcon(imageInfoReader.getImage(imageId, null)));
    }
}
