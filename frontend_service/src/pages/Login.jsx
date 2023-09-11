import React from "react";
import {useUser} from "hook/User";
import styled from "styled-components";
import {useForm} from "react-hook-form";
import {login} from "backend/idm";
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

/**
 * useUser():
 * <br>
 * This is a hook we will use to keep track of our accessToken and
 * refreshToken given to use when the user calls "login".
 * <br>
 * For now, it is not being used, but we recommend setting the two tokens
 * here to the tokens you get when the user completes the login call (once
 * you are in the .then() function after calling login)
 * <br>
 * These have logic inside them to make sure the accessToken and
 * refreshToken are saved into the local storage of the web browser
 * allowing you to keep values alive even when the user leaves the website
 * <br>
 * <br>
 * useForm()
 * <br>
 * This is a library that helps us with gathering input values from our
 * users.
 * <br>
 * Whenever we make a html component that takes a value (<input>, <select>,
 * ect) we call this function in this way:
 * <pre>
 *     {...register("email")}
 * </pre>
 * Notice that we have "{}" with a function call that has "..." before it.
 * This is just a way to take all the stuff that is returned by register
 * and <i>distribute</i> it as attributes for our components. Do not worry
 * too much about the specifics of it, if you would like you can read up
 * more about it on "react-hook-form"'s documentation:
 * <br>
 * <a href="https://react-hook-form.com/">React Hook Form</a>.
 * <br>
 * Their documentation is very detailed and goes into all of these functions
 * with great examples. But to keep things simple: Whenever we have a html with
 * input we will use that function with the name associated with that input,
 * and when we want to get the value in that input we call:
 * <pre>
 * getValue("email")
 * </pre>
 * <br>
 * To Execute some function when the user asks we use:
 * <pre>
 *     handleSubmit(ourFunctionToExecute)
 * </pre>
 * This wraps our function and does some "pre-checks" before (This is useful if
 * you want to do some input validation, more of that in their documentation)
 */
const Login = () => {
    const {
        accessToken, setAccessToken,
        refreshToken, setRefreshToken
    } = useUser();

    const {register, getValues,  formState: { errors }, handleSubmit} = useForm();

    const navigate = useNavigate(); // We can set and get our query params with this

    const moveToRegister = () => {
        navigate("/register");
    };
    const submitLogin = () => {
        const email = getValues("email");
        const password = getValues("password");

        const payLoad = {
            email: email,
            password: password.split('')
        }


        login(payLoad)
            .then(response => {setAccessToken(response.data.accessToken); navigate("/")}) //alert(JSON.stringify(response.data, null, 2)))
            .catch(error => alert(JSON.stringify(error.response.data, null, 2)))
    }

    return (
        <StyledDiv>
            <StyledField>
                <StyledLegend> <h2>Login</h2> </StyledLegend>
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
            <StyledButton onClick={handleSubmit(submitLogin)}>Login</StyledButton>
            <Styledp>Don't have an account? Register <StyledRerouteButton onClick={moveToRegister}> Here! </StyledRerouteButton> </Styledp>
        </StyledDiv>

    );
}

export default Login;
