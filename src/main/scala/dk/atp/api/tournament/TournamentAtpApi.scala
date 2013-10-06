package dk.atp.api.tournament

import TournamentAtpApi._
import java.util.Date
import dk.atp.api.domain.SurfaceEnum._

/**
 * Obtains all tennis tournaments from http://www.atpworldtour.com/ page for a given year.
 *
 * @author korzekwad
 */
object TournamentAtpApi {

  /**
   * @param numOfSet 2 (THREE_SET_MATCH) or 3 (FIVE_SET_MATCH)
   */
  case class Tournament(tournamentTime: Date, tournamentName: String, surface: SurfaceEnum, numOfSet: Int, tournamentUrl: String)
  case class Match(score: String, matchFactsUrl: String)

  case class MatchFacts(playerAFacts: PlayerFacts, playerBFacts: PlayerFacts, winner: String, round: String, durationMinutes: Int) {

    def playerFacts(playerName: String): Option[PlayerFacts] = {
      val facts = playerName match {
        case playerAFacts.playerName => Option(playerAFacts)
        case playerBFacts.playerName => Option(playerBFacts)
        case _ => None
      }
      facts
    }

    def playerOpponentFacts(playerName: String): Option[PlayerFacts] = {
      val facts = playerName match {
        case playerAFacts.playerName => Option(playerBFacts)
        case playerBFacts.playerName => Option(playerAFacts)
        case _ => None
      }
      facts
    }

    def containsPlayer(playerName: String): Boolean = (playerAFacts.playerName :: playerBFacts.playerName :: Nil).contains(playerName)
  }

  case class PlayerFacts(playerName: String,
    aces: Int,
    doubleFaults: Int,
    firstServeHits: Int, firstServeTotal: Int,
    firstServeWon: Int, firstServePlayed: Int,
    secondServeWon: Int, secondServePlayed: Int,
    breakPointsSaved: Int, breakPointsTotal: Int,
    serviceGames: Int,
    servicePointsWon: Int,servicePointsTotal: Int) {
    def servicePointsWonPct: Double = servicePointsWon.toDouble / servicePointsTotal.toDouble
    def servicePointsLostPct = 1 - servicePointsWonPct
  }
}

trait TournamentAtpApi {

  /**
   * @param year
   */
  def parseTournaments(year: Int): List[Tournament]

  /**
   * @param tournamentUrl, e.g. http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011
   *
   */
  def parseTournament(tournamentUrl: String): List[Match]

  /**
   * @param url e.g. http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=1&p=N409
   */
  def parseMatchFacts(matchFactsUrl: String): MatchFacts
}