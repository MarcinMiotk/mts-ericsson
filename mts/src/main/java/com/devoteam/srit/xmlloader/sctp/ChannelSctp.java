/* 
 * Copyright 2012 Devoteam http://www.devoteam.com
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * 
 * This file is part of Multi-Protocol Test Suite (MTS).
 * 
 * Multi-Protocol Test Suite (MTS) is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the
 * License.
 * 
 * Multi-Protocol Test Suite (MTS) is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Multi-Protocol Test Suite (MTS).
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.devoteam.srit.xmlloader.sctp;

import com.devoteam.srit.xmlloader.core.Parameter;
import com.devoteam.srit.xmlloader.core.ParameterKey;
import com.devoteam.srit.xmlloader.core.Runner;
import com.devoteam.srit.xmlloader.core.log.GlobalLogger;
import com.devoteam.srit.xmlloader.core.log.TextEvent.Topic;
import com.devoteam.srit.xmlloader.core.protocol.*;
import com.devoteam.srit.xmlloader.core.utils.Config;
import com.devoteam.srit.xmlloader.core.utils.Utils;

import java.util.List;

import org.dom4j.Element;

/**
 *
 * @author nghezzaz
 */

//  channel is called association in SCTP 
public abstract class ChannelSctp extends Channel
{
	/**
	 * 
	 */
    protected ListenpointSctp listenpointSctp;
    
    /**
     * the channel initialization parameters
     * to use when opening transport layer
     * can be null
     */
    protected ChannelConfigSctp configSctp;

    /** Creates a new instance of Channel*/
    public ChannelSctp(Stack stack)
    {
    	super(stack);
    }

    /** Creates a new instance of Channel*/
    public ChannelSctp(String name)
    {
    	super(name);
    	assert(false):"this code path is not tested";
    }

    /** Creates a new instance of Channel */
    public ChannelSctp(String localHost, int localPort, String remoteHost, int remotePort, String aProtocol) throws Exception
    {
    	super(localHost, localPort, remoteHost, remotePort, aProtocol);
    	assert(false):"this code path is not tested";
    }

    /** Creates a new instance of Channel */
    public ChannelSctp(String name, String localHost, String localPort, String remoteHost, String remotePort, String aProtocol) throws Exception
    {
    	super(name, localHost, localPort, remoteHost, remotePort, aProtocol);
    }

    public ChannelSctp(Listenpoint aListenpoint, String aLocalHost, int aLocalPort, String aRemoteHost, int aRemotePort, String aProtocol) throws Exception
    {
        super(aLocalHost, aLocalPort, aRemoteHost, aRemotePort, aProtocol);
        this.listenpointSctp = (ListenpointSctp)aListenpoint;
    }

    public ChannelSctp(Listenpoint aListenpoint, String name, String localHost, String localPort, String remoteHost, String remotePort, String aProtocol) throws Exception
    {
        super(name, localHost, localPort, remoteHost, remotePort, aProtocol);
        this.listenpointSctp = (ListenpointSctp)aListenpoint;
    }

    /**
     * @author emicpou
     * @param name
     * @param listenpointSctp
     * @throws Exception
     */
    protected ChannelSctp(String name,ListenpointSctp listenpointSctp) throws Exception
    {
        super(name);

        this.setLocalAddresses( listenpointSctp.getAddresses() );
        this.localPort = listenpointSctp.getPort();

        this.protocol = listenpointSctp.getProtocol();
        this.stack = listenpointSctp.getStack();

        this.listenpointSctp = listenpointSctp;
    }
    
    /**
     * the channel initialization parameters
     * to use when opening transport layer
     * can be null
     */
    //@Immutable
    public ChannelConfigSctp getConfigSctp(){
    	return this.configSctp;
    }
   
    /**
     * Open a Channel
     * should be overriden
     */
    @Override
    public boolean open() throws Exception
    {
    	//does not call super.open intentionally

    	//ensure the channel has a config
    	if( this.configSctp==null ){
    		if( this.transportInfos!=null && (this.transportInfos instanceof ChannelTransportInfosSctp) ){
    	    	//the channel will apply the higher level protocol config (set in the clone method)
    			ChannelTransportInfosSctp channelTransportInfosSctp = (ChannelTransportInfosSctp)this.transportInfos;
    			this.configSctp = channelTransportInfosSctp.getChannelConfigSctp();
    		}
    		else{
    	    	//the channel will apply the default config
	        	this.configSctp = new ChannelConfigSctp();
	        	this.configSctp.setFromSctpStackConfig();
    		}
    	}
    	assert this.configSctp!=null;

    	//other common sctp code here...

    	return true;
    }
    
    /**
     * Close a Channel
     * should be overriden
     */
    @Override
    public boolean close()
    {	
    	//does not call super.close intentionally
    	
    	//common sctp code here...

    	return true;
    }
    
    /**
     * Send a Msg to Channel
     * should be overriden
     */
    @Override
    public boolean sendMessage(Msg msg) throws Exception {
    	//does not call super.sendMessage intentionally

    	//common sctp code here...

    	return true;
    }
    
    /** Get the transport protocol */
    @Override
    public String getTransport() 
    {
    	return StackFactory.PROTOCOL_SCTP;
    }
   
    public Listenpoint getListenpointSctp() {
      return this.listenpointSctp;
    }
	
    /** 
     * Parse the message from XML element 
     * should be overriden
     */
    @Override
    public void parseFromXml(Element root, Runner runner, String protocol) throws Exception
    {
    	super.parseFromXml(root, runner, protocol);
    	
       	@SuppressWarnings("unchecked")
    	List<Element> sctpElements = root.elements("sctp");

       	this.configSctp = new ChannelConfigSctp();
       	this.configSctp.parseFromXml(sctpElements);
		
		// log datas
		GlobalLogger.instance().getApplicationLogger().debug(Topic.PROTOCOL, ""+this.getName()+":ChannelSctp#parseFromXml config="+this.configSctp);			

    	//other common sctp code here...
    }
    
    //---------------------------------------------------------------------
    // methods for the XML display / parsing
    //---------------------------------------------------------------------

    /** 
     * Returns the string description of the message. Used for logging as DEBUG level 
     */
    @Override
    public String toString()
    {
        String ret = super.toString();
        ret += this.toString_attributes();
        ret += ">\n";
        if( this.configSctp!=null ) {
        	ret += this.configSctp.toString();
        }
        return ret;
    }
    
    /**
     * should be overriden
     * @return stringified attributes
     */
    protected String toString_attributes()
    {
    	return "";
    }
   
    /**
     *  @param associationFilter optional association id (Nullable)
     */
    public abstract String toXml_LocalAddresses(AssociationSctp associationFilter) throws Exception;

    /**
     *  @param associationFilter optional association id (Nullable)
     */
    public abstract String toXml_PeerAddresses(AssociationSctp associationFilter) throws Exception;
        
    //------------------------------------------------------
    // method for the "setFromMessage" <parameter> operation
    //------------------------------------------------------
    
    /** 
     * Get a parameter from the message 
     */
    @Override
    public final Parameter getParameter(String path) throws Exception
    {
		Parameter var = super.getParameter(path);
		if (var != null)
		{
			return var;
		}

		var = new Parameter();
        path = path.trim();
        String[] params = Utils.splitPath(path);

        if(params[1].equalsIgnoreCase("peerHosts")) 
        {
        	var = this.getParameterPeerHosts();
        }
        else if(params[1].equalsIgnoreCase("peerPort")) 
        {
        	var = this.getParameterPeerPort();
        }    
        else
        {
        	Parameter.throwBadPathKeywordException(path);
        }
        return var; 
    }    
    
    /**
     * 
     * @return peer hosts adresses
     */
    protected abstract Parameter getParameterPeerHosts() throws Exception;
    
    /**
     * 
     * @return peer hosts port
     */
    protected abstract Parameter getParameterPeerPort() throws Exception;

}
