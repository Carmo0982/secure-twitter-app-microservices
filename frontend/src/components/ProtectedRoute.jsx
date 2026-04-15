import { useEffect } from 'react'
import { useAuth0 } from '@auth0/auth0-react'

export default function ProtectedRoute({ children }) {
    const { isAuthenticated, isLoading, loginWithRedirect } = useAuth0()

    useEffect(() => {
        if (isLoading || isAuthenticated) return

        loginWithRedirect({
            appState: { returnTo: window.location.pathname },
            authorizationParams: {
                redirect_uri: window.location.origin,
                audience: import.meta.env.VITE_AUTH0_AUDIENCE,
            },
        })
    }, [isAuthenticated, isLoading, loginWithRedirect])

    if (isLoading) return <p>Cargando...</p>

    if (!isAuthenticated) {
        return <p>Redirigiendo a login...</p>
    }

    return children
}