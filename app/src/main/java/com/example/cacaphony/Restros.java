package com.example.cacaphony;

public class Restros {

	private String name;
	private String ID;
	private double closing;
	private double opening;
	public Restros(String name, String ID, double opening, double closing)
	{
		this.name = name;
		this.ID = ID;
		this.closing = closing;
		this.opening = opening;
	}
	public String getName()
	{
		return name;
	}
	public String getID()
	{
		return ID;
	}
	public double getopening()
	{
		return opening;
	}
	public double getclosing()
	{
		return closing;
	}
}
