import React from "react";
import {useUser} from "hook/User";
import styled from "styled-components";
import {useForm} from "react-hook-form";
import * as IDM from "../backend/idm";
import {NavLink, useNavigate} from "react-router-dom";


const StyledDiv = styled.div`
  display: flex;
  flex-direction: column;
`
const ErrorMessage = styled.div`
  font-size: 12px;
  color: red;
`
const StyledLegend = styled.legend`
  padding: 10px;
  padding-top: 10px;
  background-color: #fff;
  border-radius: 5px;
  size = 10;
`
const StyledUl = styled.ul`
     margin: 0;
     padding: 0;
`
const StyledLi = styled.li`
    display: grid;
    align-items: center;
    margin: 10px;
`
const StyledLabel = styled.label`
    text-align: left;
    padding-bottom: 2px;
`
const StyledField = styled.fieldset`
margin: 0;
background-color: #fff;
border: none;
border-radius: 5px;
box-shadow: 0 1px 3px rgba(0,0,0,0.2);
`

const StyledInput = styled.input`
    padding: 5px;
    width: 400px;
    font-size: 14pt;
    border: 1px solid #ddd;
    border-radius: 5px;
    &:hover {
    border: 1px solid #aaf;
`
const Styledp = styled.p`
    text-align: center;
    padding-top: 5px;
`

const StyledButton = styled.button`
      padding: 10px;
      border:1px solid rgba(0,0,0,0);
      border-radius: 8px;
      background: #fff;
      box-shadow: 0 1px 5px rgba(0,0,0,5);
      &:hover {
        background-color: #eef;
        border: 1px solid #aaf;
      }
`

const StyledRerouteButton = styled.button`
  border: none;
  font-size: 17px;
  color: blue;
  justify-content: left;
  text-decoration: none;
  background: pink;
  &:hover {
        background-color: none;
        border: 1px solid #aaf;
      }
`

const Register = () => {

    const {register, getValues, formState: { errors }, handleSubmit} = useForm();

        const navigate = useNavigate(); // We can set and get our query params with this

        const moveToLogin = () => {
            navigate("/login");
        };


    const submitRegister = () => {
        const email = getValues("email");
        const password = getValues("password");


        const payLoad = {
            email: email,
            password: password.split('')
        }


        IDM.register(payLoad)
            .then(response => {alert(JSON.stringify(response.data, null, 2)); navigate("/login")})
            .catch(error => alert(JSON.stringify(error.response.data, null, 2)))
    }


    return (
        <StyledDiv>
            <StyledField>
                <StyledLegend> <h2>Register </h2> </StyledLegend>
                <StyledUl>
                    <StyledLi>
                        <StyledLabel> Email </StyledLabel>
                        <StyledInput {...register("email", {required: true})} type={"email"}/>
                        <ErrorMessage> {errors.email && "Email is required"} </ErrorMessage>
                    </StyledLi>
                    <StyledLi>
                        <StyledLabel> Password </StyledLabel>
                        <StyledInput {...register("password", {required: true})} type={"password"}/>
                        <ErrorMessage> {errors.password && "Password is required"} </ErrorMessage>
                    </StyledLi>
                </StyledUl>
            </StyledField>
            <StyledButton onClick={handleSubmit(submitRegister)}>Sign Up!</StyledButton>
            <Styledp>Already Have an Account? Login <StyledRerouteButton onClick={moveToLogin}> Here! </StyledRerouteButton> </Styledp>

        </StyledDiv>
    );
}

export default Register;
